package com.ziqiang.sushuodorm.daos;

import com.ziqiang.sushuodorm.entity.dto.post.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {
    List<PostEsDTO> findByUserId(Long userId);

    List<PostEsDTO> findByTitle(String title);

    List<PostEsDTO> findByTitleOrContent(String title, String content);
}
