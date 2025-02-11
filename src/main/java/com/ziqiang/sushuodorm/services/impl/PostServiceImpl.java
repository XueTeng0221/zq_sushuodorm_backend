package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import java.util.stream.Collectors;

@Service
@EqualsAndHashCode(callSuper = false)
@Data
public class PostServiceImpl extends ServiceImpl<PostMapper, PostItem> implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public boolean insertPost(String title) {
        PostItem postItem = new PostItem()
                .setTitle(title)
                .setIsDeleted(false)
                .setComments(new HashSet<>());
        return postMapper.insert(postItem) > 0;
    }

    @Override
    public boolean insertPost(String title, List<String> tags) {
        PostItem postItem = new PostItem()
                .setTitle(title)
                .setIsDeleted(false)
                .setComments(new HashSet<>())
                .setTags(String.join(",", tags));
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
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getPostId, postId);
        return new HashSet<>(commentMapper.selectList(queryWrapper));
    }

    @Override
    public IPage<CommentVo> getCommentByPostId(Long postId, PostQueryRequest postQueryRequest) {
        return commentMapper.selectPage(
                new Page<>(postQueryRequest.getCurrentId(), postQueryRequest.getPageSize()),
                new QueryChainWrapper<>(commentMapper).lambda().eq(CommentItem::getPostId, postId)
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
    public Page<PostVo> getPosts(PostQueryRequest postQueryRequest) throws NoSuchPostException {
        LambdaQueryChainWrapper<PostItem> queryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .like(PostItem::getTitle, postQueryRequest.getTitle())
                .like(PostItem::getTags, String.join(", ", postQueryRequest.getTags()));
        List<PostVo> postVoList = new ArrayList<>();
        List<PostItem> postItems = postMapper.selectList(queryWrapper);
        postItems.forEach(postItem -> postVoList.add(new PostVo()
                        .setDate(postItem.getDate())
                        .setAuthor(postItem.getAuthor())
                        .setContent(postItem.getContent())
                        .setTitle(postItem.getTitle())
                        .setDate(postItem.getDate())
                        .setComments(postItem.getComments())
                        .setTags(Arrays.stream(postItem.getTags().split(", ")).toList())));
        Page<PostVo> page = new Page<>(postQueryRequest.getCurrentId(), postQueryRequest.getPageSize());
        page.setRecords(postVoList);
        return page;
    }

    @Override
    public Page<PostVo> searchItemByPostId(List<String> keywords, PostQueryRequest postQueryRequest) throws NoSuchPostException {
        LambdaQueryChainWrapper<PostItem> queryWrapper = new QueryChainWrapper<>(postMapper).lambda();
        keywords.forEach(keyword -> queryWrapper.or().like(PostItem::getTitle, keyword));
        List<PostVo> postVoList = new ArrayList<>();
        List<PostItem> postItems = postMapper.selectList(queryWrapper);
        postItems.forEach(postItem -> postVoList.add(new PostVo()
                .setDate(postItem.getDate())
                .setAuthor(postItem.getAuthor())
                .setContent(postItem.getContent())
                .setTitle(postItem.getTitle())
                .setDate(postItem.getDate())
                .setComments(postItem.getComments())
                .setTags(Arrays.stream(postItem.getTags().split(", ")).toList())));
        Page<PostVo> page = new Page<>(postQueryRequest.getCurrentId(), postQueryRequest.getPageSize());
        page.setRecords(postVoList);
        return page;
    }

    @Override
    public List<PostItem> getItemByPostId(Long postId) throws NoSuchPostException {
        LambdaQueryChainWrapper<PostItem> queryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, postId);
        return postMapper.selectList(queryWrapper);
    }

    @Override
    public PostVo getPostById(Long postId) throws NoSuchPostException {
        LambdaQueryChainWrapper<PostItem> queryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, postId);
        PostItem postItem = postMapper.selectOne(queryWrapper);
        return new PostVo().setTitle(postItem.getTitle())
                        .setTags(Arrays.stream(postItem.getTags().split(", ")).toList())
                        .setContent(postItem.getContent())
                        .setAuthor(postItem.getAuthor())
                        .setDate(postItem.getDate())
                        .setLikes(postItem.getLikes());
    }

    @Override
    public IPage<PostVo> getItemByUsername(String username, PostQueryRequest postQueryRequest) {
        LambdaQueryChainWrapper<PostItem> queryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getAuthor, username);
        List<PostItem> postItems = postMapper.selectList(queryWrapper);
        Map<String, String> titleTagMap = postItems.stream().collect(
                Collectors.toMap(PostItem::getTitle, PostItem::getTags, (s1, s2) -> s1, HashMap::new));
        return postMapper.selectPage(new Page<>(postQueryRequest.getCurrentId(), postQueryRequest.getPageSize()), queryWrapper)
                .convert(postItem -> new PostVo()
                    .setTitle(postItem.getTitle())
                    .setTags(Arrays.stream(titleTagMap.getOrDefault(postItem.getTitle(), "").split(", ")).toList())
                    .setContent(postItem.getContent())
                    .setAuthor(postItem.getAuthor())
                    .setDate(postItem.getDate())
                    .setLikes(postItem.getLikes())
                    .setComments(postItem.getComments())
                );
    }
}
