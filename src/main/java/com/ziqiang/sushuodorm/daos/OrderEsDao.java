package com.ziqiang.sushuodorm.daos;

import com.ziqiang.sushuodorm.entity.dto.item.OrderEsDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEsDao extends ElasticsearchRepository<OrderEsDTO, String> {
    OrderEsDTO findByOrderId(String orderId, Sort sort);
}
