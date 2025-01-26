package com.ziqiang.sushuodorm.daos;

import com.ziqiang.sushuodorm.entity.dto.post.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {
    List<PostEsDTO> findByUserId(Long userId);
}
