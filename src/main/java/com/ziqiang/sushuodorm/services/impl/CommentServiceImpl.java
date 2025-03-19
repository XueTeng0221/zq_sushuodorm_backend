package com.ziqiang.sushuodorm.services.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziqiang.sushuodorm.entity.dto.comment.CommentQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;
import com.ziqiang.sushuodorm.exception.NoSuchCommentException;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;
import com.ziqiang.sushuodorm.mapper.*;
import com.ziqiang.sushuodorm.services.CommentService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;

@Service
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

    private Map<Long, Set<CommentItem>> commentTree = new HashMap<>();

    public Optional<CommentItem> selectOptComment(Wrapper<CommentItem> commentItemWrapper) {
        return Optional.ofNullable(commentMapper.selectOne(commentItemWrapper));
    }
    
    public Optional<PostItem> selectOptPost(Wrapper<PostItem> postItemWrapper) {
        return Optional.ofNullable(postMapper.selectOne(postItemWrapper));
    }
    
    @Override
    public boolean addComment(Date date, Long postId, String username, String content) throws NoSuchPostException {
        LambdaQueryChainWrapper<PostItem> postQueryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, postId);
        PostItem postItem = selectOptPost(postQueryWrapper).orElseThrow(NoSuchPostException::new);
        CommentItem commentItem = new CommentItem()
                .setDate(date)
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
    public boolean addReply(Date date, Long commentId, String replierName, String content) throws NoSuchPostException, NoSuchCommentException {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getId, commentId);
        CommentItem commentItem = selectOptComment(queryWrapper).orElseThrow(NoSuchCommentException::new);

        LambdaQueryChainWrapper<PostItem> postQueryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, commentItem.getPostId());
        PostItem postItem = selectOptPost(postQueryWrapper).orElseThrow(NoSuchPostException::new);

        CommentItem replyItem = new CommentItem()
                .setDate(date)
                .setAuthor(replierName)
                .setParentId(commentItem.getPostId())
                .setId(commentItem.getPostId())
                .setContent(content)
                .setReplies(new HashSet<>());
        postItem.getComments().add(replyItem);
        commentTree.get(commentItem.getId()).add(replyItem);
        commentTree.put(replyItem.getId(), new HashSet<>());
        return commentMapper.insertOrUpdate(replyItem) && postMapper.insertOrUpdate(postItem);
    }

    @Override
    public boolean deleteComment(Long commentId, Long postId) throws NoSuchPostException, NoSuchCommentException {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getId, commentId)
                .eq(CommentItem::getPostId, postId);
        CommentItem commentItem = selectOptComment(queryWrapper).orElseThrow(NoSuchCommentException::new);

        LambdaQueryChainWrapper<PostItem> postQueryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, commentItem.getPostId().equals(postId) ? commentItem.getPostId() : -1L);
        PostItem postItem = selectOptPost(postQueryWrapper).orElseThrow(NoSuchPostException::new);

        commentItem.getReplies().forEach(item -> item.setParentId(-1L));
        commentTree.get(commentItem.getId()).clear();
        if (commentItem.getParentId() != -1L) {
            postItem.getComments().remove(commentItem);
            commentTree.get(commentItem.getParentId()).remove(commentItem);
        }
        return commentMapper.deleteById(commentItem) > 0 && postMapper.insertOrUpdate(postItem);
    }

    @Override
    public CommentVo getComment(Long commentId) throws NoSuchCommentException {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getId, commentId);
        CommentItem commentItem = selectOptComment(queryWrapper).orElseThrow(NoSuchCommentException::new);
        return new CommentVo()
                .setId(commentItem.getId())
                .setContent(commentItem.getContent())
                .setAuthor(commentItem.getAuthor())
                .setDate(commentItem.getDate())
                .setLikes(commentItem.getLikes());
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
                .setLikes(commentItem.getLikes()));
    }

    @Override
    @Transactional
    public IPage<CommentVo> getAllComments(List<String> keywords, CommentQueryRequest queryRequest) {
        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda();
        keywords.forEach(keyword -> queryWrapper.or().like(CommentItem::getContent, keyword));
        List<CommentItem> commentItems = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVos = new ArrayList<>();
        Page<CommentVo> page = new Page<>(queryRequest.getCurrentId(), queryRequest.getPageSize());
        commentItems.forEach(commentItem -> commentVos.add(new CommentVo()
                .setId(commentItem.getId())
                .setContent(commentItem.getContent())
                .setAuthor(commentItem.getAuthor())
                .setDate(commentItem.getDate())
                .setLikes(commentItem.getLikes())));
        page.setRecords(commentVos);
        return page;
    }

    @Override
    @Transactional
    public IPage<CommentVo> getAllRepliesByCommentId(String username, Long postId, Long commentId, CommentQueryRequest queryRequest)
        throws NoSuchPostException, NoSuchCommentException {
        // 查询
        LambdaQueryChainWrapper<PostItem> postQueryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, postId);
        PostItem postItem = selectOptPost(postQueryWrapper).orElseThrow(NoSuchPostException::new);

        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getId, commentId)
                .eq(CommentItem::getPostId, postItem.getId())
                .orderByDesc(CommentItem::getDate);
        CommentItem selectedComment = selectOptComment(queryWrapper).orElseThrow(NoSuchCommentException::new);
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
    public int getReplyCount(Long commentId, Long postId) throws NoSuchPostException, NoSuchCommentException {
        // 查询
        LambdaQueryChainWrapper<PostItem> postQueryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, postId);
        PostItem postItem = selectOptPost(postQueryWrapper).orElseThrow(NoSuchPostException::new);

        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getId, commentId)
                .eq(CommentItem::getPostId, postItem.getId())
                .orderByDesc(CommentItem::getDate);
        CommentItem selectedComment = selectOptComment(queryWrapper).orElseThrow(NoSuchCommentException::new);
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
    public IPage<CommentVo> getAllRepliesByUser(String replierName, String username, Long postId, CommentQueryRequest commentQueryRequest)
        throws NoSuchPostException {
        // 查询
        LambdaQueryChainWrapper<PostItem> postQueryWrapper = new QueryChainWrapper<>(postMapper).lambda()
                .eq(PostItem::getId, postId);
        PostItem postItem = selectOptPost(postQueryWrapper).orElseThrow(NoSuchPostException::new);

        LambdaQueryChainWrapper<CommentItem> queryWrapper = new QueryChainWrapper<>(commentMapper).lambda()
                .eq(CommentItem::getAuthor, replierName)
                .eq(CommentItem::getPostId, postItem.getId())
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
        // 实体转化为Vo
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