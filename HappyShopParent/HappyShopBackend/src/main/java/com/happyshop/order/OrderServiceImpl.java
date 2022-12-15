package com.happyshop.order;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Order;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository repo;
    
    @Override
    public Page<Order> findAll(String keyword, Pageable pageable){
        if (!keyword.isBlank()) {
            return repo.searchOrder(keyword, pageable);
        }
        return repo.findAll(pageable);
    }

    public Optional<Order> findById(Integer id) {
        return repo.findById(id);
    }

    public <S extends Order> S save(S entity) {
        return repo.save(entity);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
       
}
