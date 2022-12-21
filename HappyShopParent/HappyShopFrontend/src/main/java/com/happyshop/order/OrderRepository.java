package com.happyshop.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.order.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
}
