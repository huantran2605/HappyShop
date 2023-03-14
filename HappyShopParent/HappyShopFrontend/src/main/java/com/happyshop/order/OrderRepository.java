package com.happyshop.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.order.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderDetails od JOIN od.product p "
            + "WHERE o.customer.id = ?2 "
            + "AND (p.name LIKE %?1% OR o.status LIKE %?1%)")
    public Page<Order> findAll(String keyword, Integer customerId, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.customer.id = ?1")
    public Page<Order> findAll(Integer customerId, Pageable pageable);     
        
    @Query("SELECT o FROM Order o WHERE o.id = ?1 AND o.customer.id = ?2")  
    public Order findByOrderIdAndCustomer(Integer orderId, Integer customerId);
    
    @Query("SELECT o FROM Order o WHERE o.customer = ?1")
    public List<Order> findAll(Customer customer);    
    
}
