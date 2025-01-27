package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.common.ErrorCode;
import com.ziqiang.sushuodorm.daos.CommentEsDao;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.mapper.*;
import com.ziqiang.sushuodorm.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
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

    private Map<Long, Set<CommentItem>> commentTree = new HashMap<>();
    private Deque<CommentItem> commentStack = new ArrayDeque<>();

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
    public IPage<CommentVo> getAllComments(String author, CommentQueryRequest queryRequest) {
        return commentMapper.selectPage(
                queryRequest.getPage(),
                new QueryChainWrapper<>(commentMapper).eq("author", author)
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
    public IPage<CommentVo> getAllComments(List<String> keywords, CommentQueryRequest queryRequest) {
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper);
        if (CollectionUtils.isEmpty(keywords)) {
            queryWrapper.like("content", keywords);
        } else {
            queryWrapper.like("content", keywords.get(0));
            keywords.forEach(keyword -> queryWrapper.or().like("content", keyword));
        }
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        return commentMapper.selectPage(
                queryRequest.getPage(),
                queryWrapper.in("id", commentItems.stream().map(CommentItem::getId).toList())
        ).convert(commentItem -> new CommentVo()
                .setId(commentItem.getId())
                .setContent(commentItem.getContent())
                .setAuthor(commentItem.getAuthor())
                .setDate(commentItem.getDate())
                .setLikes(commentItem.getLikes())
        );
    }

    @Override
    public IPage<CommentVo> getAllReplies(String username, Long commentId, CommentQueryRequest queryRequest) {
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper)
                .eq("author", username)
                .eq("id", commentId);
        commentStack.push(commentMapper.selectOne(queryWrapper));
        while (!commentStack.isEmpty()) {
            CommentItem commentItem = commentStack.pop();
            commentStack.addAll(commentTree.getOrDefault(commentItem.getId(), new HashSet<>()));
            queryWrapper.or().eq("author", commentItem.getAuthor());
        }
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        return commentMapper.selectPage(
                queryRequest.getPage(),
                queryWrapper.in("author", commentItems.stream().map(CommentItem::getAuthor).toList())
        ).convert(commentItem -> new CommentVo()
                .setId(commentItem.getId())
                .setContent(commentItem.getContent())
                .setAuthor(commentItem.getAuthor())
                .setDate(commentItem.getDate())
                .setLikes(commentItem.getLikes())
        );
    }

    @Override
    public int getReplyCount(Long commentId, Long postId) {
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper)
                .eq("post_id", postId);
        return commentMapper.selectOne(queryWrapper).getReplies().size();
    }

    @Override
    public IPage<CommentVo> getAllReplies(String replierName, String username, CommentQueryRequest queryRequest) {
        QueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).eq("author", replierName);
        commentStack.push(commentMapper.selectOne(queryWrapper));
        while (!commentStack.isEmpty()) {
            CommentItem commentItem = commentStack.pop();
            commentStack.addAll(commentTree.getOrDefault(commentItem.getId(), new HashSet<>()));
            queryWrapper.or().eq("author", commentItem.getAuthor());
        }
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        List<CommentItem> relatedComments = commentMapper.selectList(new QueryChainWrapper<>(commentMapper)
                .allEq(Map.of("author", username, "reply_num", commentItems.stream().map(CommentItem::getId).toList())));
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
    public List<CommentItem> findComments(Long postId, CommentQueryRequest queryRequest) {
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