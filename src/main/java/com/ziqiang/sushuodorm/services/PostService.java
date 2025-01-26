package com.ziqiang.sushuodorm.services;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ziqiang.sushuodorm.entity.dto.post.PostQueryRequest;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import com.ziqiang.sushuodorm.entity.item.PostItem;
import com.ziqiang.sushuodorm.entity.vo.CommentVo;
import com.ziqiang.sushuodorm.entity.vo.PostVo;
import com.ziqiang.sushuodorm.exception.NoSuchPostException;

import java.util.List;
import java.util.Set;

public interface PostService extends IService<PostItem> {
    boolean updatePost(PostItem postItem);

    boolean removePost(PostItem postItem);

    boolean insertPost(String title);

    boolean insertPost(String title, List<String> tags);

    PostVo getPostById(Long postId) throws NoSuchPostException;

    Set<CommentItem> getCommentByPostId(Long postId);

    IPage<CommentVo> getCommentByPostId(Long postId, PostQueryRequest postQueryRequest);

    Page<PostItem> getPosts(PostQueryRequest postQueryRequest) throws NoSuchPostException;

    Page<PostItem> searchItemByPostId(List<String> keywords, int pageFrom, int pageSize) throws NoSuchPostException;

    List<PostItem> getItemByPostId(Long postId);

    List<PostItem> getItemByPostId(Long postId, PostQueryRequest postQueryRequest);

    IPage<PostVo> getItemByUsername(String username, PostQueryRequest postQueryRequest);
}
