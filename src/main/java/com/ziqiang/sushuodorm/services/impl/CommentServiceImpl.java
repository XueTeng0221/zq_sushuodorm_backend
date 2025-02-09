package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.daos.CommentEsDao;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.mapper.*;
import com.ziqiang.sushuodorm.services.CommentService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Data
@EqualsAndHashCode(callSuper = false)
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

    private final Map<Long, Set<CommentItem>> commentTree = new HashMap<>();

    @Override
    public boolean addComment(Long postId, String username, String content) {
        LambdaQueryChainWrapper<PostItem> queryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, postId);
        PostItem postItem = postMapper.selectOne(queryWrapper);
        CommentItem commentItem = new CommentItem()
                .setAuthor(username)
                .setPostId(postId)
                .setParentId(-1L)
                .setContent(content)
                .setReplies(new HashSet<>());
        postItem.getComments().add(commentItem);
        commentTree.put(commentItem.getId(), new HashSet<>());
        return commentMapper.insertOrUpdate(commentItem) && postMapper.insertOrUpdate(postItem);
    }

    @Override
    public boolean addReply(Long commentId, String replierName, String content) throws NoSuchPostException {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getId, commentId);
        CommentItem commentItem = commentMapper.selectOne(queryWrapper);

        LambdaQueryChainWrapper<PostItem> postQueryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, commentItem.getPostId());
        PostItem postItem = postMapper.selectOne(postQueryWrapper);

        CommentItem replyItem = new CommentItem()
                .setAuthor(replierName)
                .setParentId(commentId)
                .setContent(content)
                .setReplies(new HashSet<>());
        postItem.getComments().add(replyItem);
        commentTree.get(commentItem.getId()).add(replyItem);
        commentTree.put(replyItem.getId(), new HashSet<>());
        return commentMapper.insertOrUpdate(replyItem) && postMapper.insertOrUpdate(postItem);
    }

    @Override
    public boolean deleteComment(Long commentId, Long postId) throws NoSuchPostException {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getId, commentId)
                .eq(CommentItem::getPostId, postId);
        CommentItem commentItem = commentMapper.selectOne(queryWrapper);

        LambdaQueryChainWrapper<PostItem> postQueryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, commentItem.getPostId().equals(postId) ? commentItem.getPostId() : -1L);
        PostItem postItem = postMapper.selectOne(postQueryWrapper);

        commentItem.getReplies().forEach(item -> item.setParentId(-1L));
        commentTree.get(commentItem.getId()).clear();
        if (commentItem.getParentId() != -1L) {
            postItem.getComments().remove(commentItem);
            commentTree.get(commentItem.getParentId()).remove(commentItem);
        }
        return commentMapper.deleteById(commentItem) > 0 && postMapper.insertOrUpdate(postItem);
    }

    @Override
    public CommentVo getComment(Long commentId) throws NoSuchPostException {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getId, commentId);
        CommentItem commentItem = commentMapper.selectOne(queryWrapper);
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
        return commentMapper.selectPage(queryRequest.getPage(),
                new QueryChainWrapper<>(commentMapper).lambda().eq(CommentItem::getAuthor, author)
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
    @Transactional
    public IPage<CommentVo> getAllComments(List<String> keywords, CommentQueryRequest queryRequest) {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda();
        if (CollectionUtils.isEmpty(keywords)) {
            queryWrapper.like(CommentItem::getContent, keywords);
        } else {
            queryWrapper.like(CommentItem::getContent, keywords.get(0));
            keywords.forEach(keyword -> queryWrapper.or().like(CommentItem::getContent, keyword));
        }
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        return commentMapper.selectPage(queryRequest.getPage(),
                queryWrapper.in(CommentItem::getId, commentItems.stream().map(CommentItem::getId).toList())
        ).convert(commentItem -> new CommentVo()
                .setId(commentItem.getId())
                .setContent(commentItem.getContent())
                .setAuthor(commentItem.getAuthor())
                .setDate(commentItem.getDate())
                .setLikes(commentItem.getLikes())
        );
    }

    @Override
    @Transactional
    public IPage<CommentVo> getAllRepliesByCommentId(String username, Long commentId, CommentQueryRequest queryRequest) {
        // 查询
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getId, commentId)
                .orderByDesc(CommentItem::getDate);
        CommentItem selectedComment = commentMapper.selectOne(queryWrapper);
        // DFS
        List<CommentItem> commentItems = new ArrayList<>();
        Set<CommentItem> visitedCommentSet = new HashSet<>();
        Deque<CommentItem> commentStack = new ArrayDeque<>();
        commentStack.push(selectedComment);
        visitedCommentSet.add(selectedComment);
        while (!commentStack.isEmpty()) {
            CommentItem commentItem = commentStack.pop();
            if (commentItem.getParentId() != -1L) {
                List<CommentItem> children = commentTree.getOrDefault(commentItem.getParentId(), new HashSet<>())
                        .stream().filter(item -> !visitedCommentSet.contains(item)).toList();
                commentStack.addAll(children);
                commentItems.addAll(children);
                visitedCommentSet.add(commentStack.peek());
            }
        }
        // 实体转化为Vo
        List<CommentVo> commentVos = new ArrayList<>();
        commentItems.forEach(commentItem -> commentVos.add(new CommentVo()
                .setId(commentItem.getId())
                .setContent(commentItem.getContent())
                .setAuthor(commentItem.getAuthor())
                .setDate(commentItem.getDate())
                .setLikes(commentItem.getLikes()))
        );
        Page<CommentVo> page = new Page<>(queryRequest.getCurrentId(), queryRequest.getPageSize());
        page.setRecords(commentVos);
        return page;
    }

    @Override
    @Transactional
    public int getReplyCount(Long commentId, Long postId) throws NoSuchPostException {
        // 查询
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getId, commentId)
                .eq(CommentItem::getPostId, postId)
                .orderByDesc(CommentItem::getDate);
        CommentItem selectedComment = commentMapper.selectOne(queryWrapper);
        // DFS计数
        int replyCount = 0;
        Deque<CommentItem> commentStack = new ArrayDeque<>();
        Set<CommentItem> visitedCommentSet = new HashSet<>();
        commentStack.push(selectedComment);
        visitedCommentSet.add(selectedComment);
        for (CommentItem tempCommentItem : commentTree.getOrDefault(selectedComment.getId(), new HashSet<>())) {
            commentStack.push(tempCommentItem);
            while (!commentStack.isEmpty()) {
                CommentItem commentItem = commentStack.pop();
                if (commentItem.getParentId() != -1L) {
                    List<CommentItem> childrenComment = commentTree.getOrDefault(commentItem.getParentId(), new HashSet<>())
                            .stream().filter(item -> !visitedCommentSet.contains(item)).toList();
                    visitedCommentSet.add(commentStack.peek());
                    replyCount += childrenComment.size();
                }
            }
        }
        return replyCount;
    }

    @Override
    @Transactional
    public IPage<CommentVo> getAllRepliesByUser(String replierName, String username, CommentQueryRequest commentQueryRequest) {
        // 查询
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getAuthor, replierName)
                .orderByDesc(CommentItem::getDate);
        List<CommentItem> selectedComments = commentMapper.selectList(queryWrapper);
        // DFS
        Deque<CommentItem> commentStack = new ArrayDeque<>();
        Set<CommentItem> visitedCommentSet = new HashSet<>();
        selectedComments.forEach(selectedComment -> {
            for (CommentItem tempCommentItem : commentTree.getOrDefault(selectedComment.getId(), new HashSet<>())) {
                commentStack.push(tempCommentItem);
                while (!commentStack.isEmpty()) {
                    CommentItem commentItem = commentStack.pop();
                    if (commentItem.getParentId() != -1L) {
                        List<CommentItem> childrenComment = commentTree.getOrDefault(commentItem.getParentId(), new HashSet<>())
                                .stream().filter(item -> !visitedCommentSet.contains(item)).toList();
                        commentStack.addAll(childrenComment);
                        visitedCommentSet.add(commentStack.peek());
                    }
                }
            }
        });
        List<CommentItem> commentItems = visitedCommentSet.stream().filter(
                commentItem -> commentItem.getAuthor().equals(username)).toList();
        List<CommentVo> commentVos = new ArrayList<>();
        commentItems.forEach(commentItem -> commentVos.add(new CommentVo()
                .setId(commentItem.getId())
                .setAuthor(commentItem.getAuthor())
                .setContent(commentItem.getContent())
                .setDate(commentItem.getDate())
                .setLikes(commentItem.getLikes()))
        );
        Page<CommentVo> page = new Page<>(commentQueryRequest.getCurrentId(), commentQueryRequest.getPageSize());
        page.setRecords(commentVos);
        return page;
    }

    @Override
    public List<CommentItem> findComments(Long postId, CommentQueryRequest queryRequest) {
        return commentMapper.selectList(
                new QueryChainWrapper<>(commentMapper).lambda()
                        .eq(CommentItem::getPostId, postId)
                        .le(CommentItem::getParentId, -1)
                        .orderByDesc(CommentItem::getDate)
                        .last("limit " + queryRequest.getCurrentId() + ", " + queryRequest.getPageSize())
        );
    }

    @Override
    public List<CommentItem> findCommentsByUsername(String username) {
        return commentMapper.selectList(
                new QueryChainWrapper<>(commentMapper).lambda()
                        .eq(CommentItem::getAuthor, username)
                        .orderByDesc(CommentItem::getDate)
        );
    }
}