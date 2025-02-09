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

    Page<PostVo> getPosts(PostQueryRequest postQueryRequest) throws NoSuchPostException;

    Page<PostVo> searchItemByPostId(List<String> keywords, PostQueryRequest postQueryRequest) throws NoSuchPostException;

    IPage<PostVo> getItemByUsername(String username, PostQueryRequest postQueryRequest);

    List<PostItem> getItemByPostId(Long postId);
}
