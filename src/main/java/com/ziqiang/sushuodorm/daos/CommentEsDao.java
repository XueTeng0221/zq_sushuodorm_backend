package com.ziqiang.sushuodorm.daos;

import com.ziqiang.sushuodorm.entity.dto.comment.CommentEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentEsDao extends ElasticsearchRepository<CommentEsDTO, Long> {
    CommentEsDTO findByCommentId(Long commentId);

    CommentEsDTO findByCommentIdAndAuthor(Long commentId, String username);

    CommentEsDTO findByCommentIdAndPostId(Long commentId, Long replyId);
}
