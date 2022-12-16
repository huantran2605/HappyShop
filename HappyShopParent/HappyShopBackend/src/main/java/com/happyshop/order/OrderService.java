package com.happyshop.order;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.order.Order;

public interface OrderService {
    int SIZE_PAGE_ORDER = 10;
    
    Page<Order> findAll(String keyword, Pageable pageable);
    
    Optional<Order> findById(Integer id) ;
    
    <S extends Order> S save(S entity);
    
    void deleteById(Integer id);
}
