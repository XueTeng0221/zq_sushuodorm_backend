package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.dto.post.PostQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;
import com.ziqiang.sushuodorm.entity.vo.PostVo;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.mapper.CommentMapper;
import com.ziqiang.sushuodorm.mapper.PostMapper;
import com.ziqiang.sushuodorm.services.PostService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@EqualsAndHashCode(callSuper = false)
@Data
public class PostServiceImpl extends ServiceImpl<PostMapper, PostItem> implements PostService {
    private PostMapper postMapper;
    private CommentMapper commentMapper;

    @Autowired
    public PostServiceImpl(PostMapper postMapper, CommentMapper commentMapper) {
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public boolean insertPost(String title) {
        PostItem postItem = new PostItem();
        postItem.setTitle(title);
        return postMapper.insert(postItem) > 0;
    }

    @Override
    public boolean insertPost(String title, List<String> tags) {
        PostItem postItem = new PostItem();
        postItem.setTitle(title);
        postItem.setTags(String.join(",", tags));
        return postMapper.insert(postItem) > 0;
    }

    @Override
    public boolean updatePost(PostItem postItem) {
        return postMapper.updateById(postItem) > 0;
    }

    @Override
    public boolean removePost(PostItem postItem) {
        return postMapper.deleteById(postItem) > 0;
    }

    @Override
    public Set<CommentItem> getCommentByPostId(Long postId) {
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        return new HashSet<>(commentMapper.selectList(queryWrapper));
    }

    @Override
    public IPage<CommentVo> getCommentByPostId(Long postId, PostQueryRequest postQueryRequest) {
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        if (commentItems.isEmpty()) {
            return new Page<>();
        }
        Map<Long, CommentItem> commentItemMap = commentItems.stream()
                .collect(Collectors.toMap(CommentItem::getId,
                        Function.identity(),
                        (existing, replacement) -> existing));
        Map<Long, CommentItem> userNameMap = commentItemMap.values().stream()
                .collect(Collectors.toMap(CommentItem::getReplyNum,
                        Function.identity(),
                        (existing, replacement) -> existing));
        commentItems.forEach(commentItem -> {
            CommentItem relatedComment = userNameMap.get(commentItem.getReplyNum());
        });
        return commentMapper.selectPage(
                new CommentQueryRequest().getPage(),
                new QueryWrapper<CommentItem>().eq("post_id", postId)
        ).convert(commentItem -> new CommentVo()
                .setId(commentItem.getId())
                .setContent(commentItem.getContent())
                .setAuthor(commentItem.getAuthor())
                .setDate(commentItem.getDate())
                .setLikes(commentItem.getLikes())
                .setUserId(commentItem.getId())
        );
    }

    @Override
    public Page<PostItem> getPosts(PostQueryRequest postQueryRequest) throws NoSuchPostException {
        QueryWrapper<PostItem> queryWrapper = new QueryWrapper<>();
        if (postQueryRequest.getTitle() != null) {
            queryWrapper.like("title", postQueryRequest.getTitle());
        }
        if (postQueryRequest.getTags() != null) {
            queryWrapper.like("tags", postQueryRequest.getTags());
        }
        Page<PostItem> page = new Page<>(postQueryRequest.getCurrentId(), postQueryRequest.getPageSize());
        return postMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Page<PostItem> searchItemByPostId(List<String> keywords, int pageFrom, int pageSize) throws NoSuchPostException {
        QueryWrapper<PostItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", keywords);
        Page<PostItem> page = new Page<>(pageFrom, pageSize);
        return postMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<PostItem> getItemByPostId(Long postId) throws NoSuchPostException {
        QueryWrapper<PostItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", postId);
        return postMapper.selectList(queryWrapper);
    }

    @Override
    public List<PostItem> getItemByPostId(Long postId, PostQueryRequest postQueryRequest) {
        QueryWrapper<PostItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", postId);
        if (postQueryRequest.getTitle() != null) {
            queryWrapper.like("title", postQueryRequest.getTitle());
        }
        if (postQueryRequest.getTags() != null) {
            queryWrapper.like("tags", postQueryRequest.getTags());
        }
        return postMapper.selectList(queryWrapper);
    }

    @Override
    public PostVo getPostById(Long postId) throws NoSuchPostException {
        PostItem postItem = postMapper.selectById(postId);
        if (postItem == null) {
            throw new NoSuchPostException(ErrorCode.CLIENT_ERROR);
        }
        return PostVo.objectToVO(postItem);
    }

    @Override
    public IPage<PostVo> getItemByUsername(String username, PostQueryRequest postQueryRequest) {
        QueryWrapper<PostItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author", username);
        List<PostItem> postItems = postMapper.selectList(queryWrapper);
        if (postItems.isEmpty()) {
            return new Page<>();
        }
        Map<String, String> titleTagMap = postItems.stream()
                .collect(Collectors.toMap(
                    item -> item.getId().toString(),
                    PostItem::getTitle,
                    (existing, replacement) -> existing,
                    HashMap::new
                ));
        IPage<PostItem> pageResult = postMapper.selectPage(postQueryRequest.getPage(), queryWrapper);
        return pageResult.convert(postItem -> new PostVo()
                .setTitle(titleTagMap.getOrDefault(postItem.getId().toString(), ""))
                .setContent(postItem.getContent())
                .setAuthor(postItem.getAuthor())
                .setDate(postItem.getDate())
                .setLikes(postItem.getLikes())
                .setComments(postItem.getComments())
        );
    }
}
