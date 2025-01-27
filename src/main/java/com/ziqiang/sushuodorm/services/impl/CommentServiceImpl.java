package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.daos.CommentEsDao;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.item.UserItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;
import com.ziqiang.sushuodorm.exception.BizException;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.mapper.*;
import com.ziqiang.sushuodorm.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private LikeCommentMapper likeCommentMapper;
    @Autowired
    private LikePostMapper likePostMapper;
    @Autowired
    private CommentEsDao commentEsDao;

    private Map<Long, Set<CommentItem>> commentTree;

    @Override
    public boolean addComment(Long postId, String username, String content) {
        CommentItem commentItem = new CommentItem()
                .setAuthor(username)
                .setPostId(postId)
                .setParentId(-1L)
                .setContent(content)
                .setReplies(new HashSet<>());
        commentMapper.insert(commentItem);
        commentTree.put(commentItem.getId(), new HashSet<>());
        return commentMapper.insertOrUpdate(commentItem);
    }

    @Override
    public boolean addReply(Long commentId, String username, String content) {
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).eq("id", commentId);
        CommentItem commentItem = queryWrapper.getEntity();
        try {
            CommentItem replyItem = new CommentItem()
                    .setId(commentId + 1)
                    .setAuthor(username)
                    .setParentId(commentId)
                    .setContent(content)
                    .setReplies(new HashSet<>());
            commentItem.setReplyNum(commentItem.getReplyNum() + 1);
            commentMapper.insert(replyItem);
            commentTree.get(commentItem.getId()).add(replyItem);
            commentTree.put(replyItem.getId(), new HashSet<>());
            return commentMapper.insertOrUpdate(replyItem);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean likeComment(Long commentId) {
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).eq("id", commentId);
        CommentItem commentItem = commentMapper.selectOne(queryWrapper);
        commentItem.setLikes(commentItem.getLikes() + 1);
        return commentMapper.updateById(commentItem) > 0;
    }

    @Override
    public boolean deleteComment(Long commentId, Long postId) {
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper)
                .eq("id", commentId)
                .eq("post_id", postId);
        if (ObjectUtils.isNull(commentMapper.selectById(commentId))) {
            throw new NoSuchPostException(ErrorCode.CLIENT_ERROR);
        }
        CommentItem commentItem = commentMapper.selectOne(queryWrapper);
        commentItem.getReplies().forEach(item -> item.setParentId(-1L));
        commentTree.get(commentItem.getId()).clear();
        if (commentItem.getParentId() != -1L) {
            commentTree.get(commentItem.getParentId()).remove(commentItem);
        }
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
                new QueryChainWrapper<>(commentMapper).eq("post_id", queryRequest.getPostId())
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
    public IPage<CommentVo> getAllComments(String username, int pageNum, int pageSize) {
        try {
            QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).eq("author", username);
            List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
            List<String> tags = commentItems.stream().map(CommentItem::getAuthor).distinct().toList();
            Map<String, UserItem> userItemMap = userMapper.selectList(new QueryChainWrapper<>(userMapper)
                    .in("username", tags)).stream().collect(
                    Collectors.toMap(UserItem::getUserName, v -> v)
            );
            commentItems.forEach(commentItem -> userItemMap.get(commentItem.getAuthor())
                    .setUserName(commentItem.getAuthor()));
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
        } catch (BizException e) {
            log.error("Error occurred while fetching comments: ", e);
            throw new NoSuchPostException(ErrorCode.CLIENT_ERROR);
        }
    }


    @Override
    public IPage<CommentVo> getAllReplies(CommentQueryRequest queryRequest) {
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
        } catch (BizException e) {
            log.error("Error occurred while fetching replies: ", e);
            throw new NoSuchPostException(ErrorCode.CLIENT_ERROR);
        }
    }

    @Override
    public IPage<CommentVo> getAllReplies(Long commentId, int pageNum, int pageSize) {
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).eq("id", commentId);
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        if (commentItems.isEmpty()) {
            return new Page<>(pageNum, pageSize).convert(commentItem -> new CommentVo());
        }
        List<Long> replyIds = commentItems.stream()
                .flatMap(commentItem -> Stream.ofNullable(commentItem.getReplyNum()))
                .distinct().collect(Collectors.toList());
        Map<Long, String> userNameMap = commentMapper.selectList(new QueryWrapper<CommentItem>()
                .in("id", replyIds))
                .stream()
                .collect(Collectors.toMap(CommentItem::getId, CommentItem::getAuthor));
        commentItems.forEach(commentItem -> {
            Long replyId = commentItem.getReplyNum();
            if (replyId != null) {
                commentItem.setAuthor(userNameMap.getOrDefault(replyId, "Unknown"));
            }
        });
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
    }

    @Override
    public IPage<CommentVo> getAllCommentsByUsername(String username) {
        QueryWrapper<CommentItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author", username);
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        Map<Long, String> userNameMap = commentMapper.selectList(new QueryWrapper<CommentItem>().
                in("id", commentItems.stream().map(CommentItem::getId).toList())).stream().collect(
                HashMap::new,
                (m, v) -> m.put(v.getId(), v.getAuthor()),
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
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).eq("author", replierName);
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        List<Long> replyIds = commentItems.stream().map(CommentItem::getReplyNum).distinct().toList();
        List<CommentItem> relatedComments = commentMapper.selectList(new QueryWrapper<CommentItem>().in("id", replyIds));
        Map<String, Map<Long, CommentItem>> userCommentMap = relatedComments.stream()
                .collect(Collectors.groupingBy(CommentItem::getAuthor,
                    Collectors.toMap(CommentItem::getPostId, Function.identity(),
                            (existing, replacement) -> existing)
                ));
        Map<Long, CommentItem> userNameMap = userCommentMap.getOrDefault(username, new HashMap<>());
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