package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.mapper.CommentMapper;
import com.ziqiang.sushuodorm.mapper.PostMapper;
import com.ziqiang.sushuodorm.mapper.UserMapper;
import com.ziqiang.sushuodorm.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@EqualsAndHashCode(callSuper = false)
@Data
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentItem> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    private Map<Long, Set<CommentItem>> commentTree;

    @Override
    public boolean addComment(Long postId) {
        CommentItem commentItem = new CommentItem()
                .setPostId(postId)
                .setParentId(-1L)
                .setReplies(new HashSet<>());

        commentMapper.insert(commentItem);
        commentTree.put(commentItem.getId(), new HashSet<>());
        return saveOrUpdate(commentItem);
    }

    @Override
    public boolean addReply(Long commentId) {
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", commentId);
        CommentItem commentItem = commentMapper.selectById(commentId);

        CommentItem replyItem = new CommentItem()
                .setId(commentId + 1)
                .setParentId(commentId)
                .setReplies(new HashSet<>());
        commentItem.setReplyNum(commentItem.getReplyNum() + 1);

        commentMapper.insert(replyItem);
        commentTree.get(commentItem.getId()).add(commentItem);
        commentTree.put(replyItem.getId(), new HashSet<>());
        return saveOrUpdate(commentItem);
    }

    @Override
    public boolean likeComment(Long commentId) {
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", commentId);

        CommentItem commentItem = commentMapper.selectById(commentId);
        commentItem.setLikes(commentItem.getLikes() + 1);
        return commentMapper.updateById(commentItem) > 0;
    }

    @Override
    public boolean deleteComment(Long commentId) {
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", commentId);
        return commentMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean deleteComment(Long commentId, Long postId) {
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", commentId).eq("post_id", postId);
        CommentItem commentItem = commentMapper.selectById(commentId);
        commentItem.setPostId(postId);
        return commentMapper.deleteById(commentItem) > 0;
    }

    @Override
    public CommentVo getComment(Long commentId) {
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", commentId);
        if (commentMapper.selectOne(queryWrapper) == null) {
            throw new NoSuchPostException(ErrorCode.CLIENT_ERROR);
        }

        CommentItem commentItem = commentMapper.selectById(commentId);
        if (commentItem.getParentId() != -1) {
            commentItem.setReplies(commentTree.get(commentItem.getParentId()));
        }
        commentItem.setReplies(commentTree.get(commentItem.getParentId()));
        return new CommentVo()
                .setId(commentItem.getId())
                .setContent(commentItem.getContent())
                .setAuthor(commentItem.getAuthor())
                .setDate(commentItem.getDate())
                .setLikes(commentItem.getLikes())
                .setUserId(commentItem.getId());
    }

    @Override
    public IPage<CommentVo> getAllComments(CommentQueryRequest queryRequest) {
        return commentMapper.selectPage(
                queryRequest.getPage(),
                new QueryWrapper<CommentItem>().
                        eq("post_id", queryRequest.getPostId())
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
    public IPage<CommentVo> getAllComments(String userName, int pageNum, int pageSize) {
        if (pageNum <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page parameters");
        }
        try {
            QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("author", userName);
            List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
            List<String> tags = commentItems.stream().map(CommentItem::getAuthor).distinct().toList();
            Map<String, UserItem> userItemMap = userMapper.selectList(new QueryWrapper<UserItem>()
                    .in("user_name", tags)).stream().collect(
                    Collectors.toMap(UserItem::getUserName, v -> v)
            );
            commentItems.forEach(commentItem -> userItemMap.get(commentItem.getAuthor()).setUserName(commentItem.getAuthor()));
            return commentMapper.selectPage(
                    new Page<>(pageNum, pageSize), queryWrapper
            ).convert(commentItem -> new CommentVo()
                    .setId(commentItem.getId())
                    .setContent(commentItem.getContent())
                    .setAuthor(commentItem.getAuthor())
                    .setDate(commentItem.getDate())
                    .setLikes(commentItem.getLikes())
                    .setUserId(commentItem.getId())
            );
        } catch (Exception e) {
            log.error("Error occurred while fetching comments: ", e);
            throw new RuntimeException("Failed to fetch comments", e);
        }
    }


    @Override
    public IPage<CommentVo> getAllReplies(CommentQueryRequest queryRequest) {
        if (queryRequest == null || queryRequest.getCommentId() == null) {
            throw new IllegalArgumentException("Invalid input: CommentId cannot be null");
        }
        try {
            QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", queryRequest.getCommentId());
            List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
            List<Long> commentIds = commentItems.stream().map(CommentItem::getId).toList();
            Map<Long, String> userNameMap = commentMapper.selectList(new QueryWrapper<CommentItem>()
                    .in("id", commentIds))
                    .stream()
                    .collect(Collectors.toMap(CommentItem::getId, CommentItem::getAuthor));
            commentItems.forEach(commentItem -> {
                String author = userNameMap.getOrDefault(commentItem.getId(), "Unknown");
                commentItem.setAuthor(author);
            });
            return commentMapper.selectPage(
                    queryRequest.getPage(), queryWrapper
            ).convert(commentItem -> new CommentVo()
                    .setId(commentItem.getId())
                    .setContent(commentItem.getContent())
                    .setAuthor(commentItem.getAuthor())
                    .setDate(commentItem.getDate())
                    .setLikes(commentItem.getLikes())
                    .setUserId(commentItem.getId())
            );
        } catch (Exception e) {
            log.error("Error occurred while fetching replies: ", e);
            throw new RuntimeException("Failed to fetch replies", e);
        }
    }


    @Override
    public IPage<CommentVo> getAllReplies(Long commentId, int pageNum, int pageId) {
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", commentId);
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        List<Long> replyNums = commentItems.stream().map(CommentItem::getReplyNum).toList();
        Map<Long, String> userNameMap = commentMapper.selectList(new QueryWrapper<CommentItem>().
                in("id", replyNums)).stream().collect(
                HashMap::new,
                (m, v) -> m.put(v.getPostId(), v.getAuthor()),
                HashMap::putAll
        );
        commentItems.forEach(commentItem -> commentItem.setAuthor(userNameMap.get(commentItem.getId())));
        return commentMapper.selectPage(
                new Page<>(pageNum, pageId), queryWrapper
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
    public IPage<CommentVo> getAllCommentsByUsername(String username) {
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author", username);
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        Map<Long, String> userNameMap = commentMapper.selectList(new QueryWrapper<CommentItem>().
                in("id", commentItems.stream().map(CommentItem::getId).toList())).stream().collect(
                HashMap::new,
                (m, v) -> m.put(v.getPostId(), v.getAuthor()),
                HashMap::putAll
        );
        commentItems.forEach(commentItem -> commentItem.setAuthor(userNameMap.get(commentItem.getId())));
        return commentMapper.selectPage(
                new CommentQueryRequest().getPage(),
                new QueryWrapper<CommentItem>().eq("author", username)
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
    public IPage<CommentVo> getAllRepliesByUsername(String replierName, String username) {
        if (replierName.isEmpty() || username.isEmpty()) {
            throw new IllegalArgumentException("replierName and username cannot be null or empty");
        }
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author", replierName);
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        if (commentItems.isEmpty()) {
            return new Page<>();
        }
        List<Long> replyIds = commentItems.stream().map(CommentItem::getReplyNum).distinct().toList();
        List<CommentItem> relatedComments = commentMapper.selectList(new QueryWrapper<CommentItem>().in("id", replyIds));
        Map<String, Map<Long, CommentItem>> userCommentMap = relatedComments.stream()
                .collect(Collectors.groupingBy(
                    CommentItem::getAuthor,
                    Collectors.toMap(CommentItem::getPostId, Function.identity(), (existing, replacement) -> existing)
                ));
        Map<Long, CommentItem> userNameMap = userCommentMap.getOrDefault(username, Collections.emptyMap());
        commentItems.forEach(commentItem -> {
            CommentItem relatedComment = userNameMap.get(commentItem.getReplyNum());
            if (!commentTree.get(relatedComment.getId()).isEmpty()) {
                commentItem.setAuthor(relatedComment.getAuthor());
            }
        });
        return commentMapper.selectPage(
                new CommentQueryRequest().getPage(),
                new QueryWrapper<CommentItem>().eq("author", username)
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
    public List<CommentItem> findComments(CommentQueryRequest queryRequest, Long postId) {
        return commentMapper.selectList(
                new QueryWrapper<CommentItem>()
                        .eq("post_id", postId)
                        .eq("parent_id", 0)
                        .orderByDesc("date")
                        .last("limit " + queryRequest.getCurrentId() + ", " + queryRequest.getPageSize())
        );
    }

    @Override
    public List<CommentItem> findCommentsByUsername(String username) {
        return commentMapper.selectList(
                new QueryWrapper<CommentItem>()
                        .eq("author", username)
                        .orderByDesc("date")
        );
    }
}