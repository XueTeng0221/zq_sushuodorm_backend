package com.ziqiang.sushuodorm.daos;

import com.ziqiang.sushuodorm.entity.dto.comment.CommentEsDTO;
import com.ziqiang.sushuodorm.entity.item.CommentItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentEsDao extends ElasticsearchRepository<CommentEsDTO, Long> {
    CommentEsDTO findByCommentId(Long commentId);

    CommentEsDTO findByCommentIdAndAuthor(Long commentId, String username);

    CommentEsDTO findByCommentIdAndPostId(Long commentId, Long replyId);
}
