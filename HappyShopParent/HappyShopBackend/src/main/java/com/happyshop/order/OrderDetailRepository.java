package com.happyshop.order;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.happyshop.common.entity.order.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>  {
    
    @Query("SELECT NEW com.happyshop.common.entity.order.OrderDetail(d.product.category.name,"
            + " d.productCost, d.shippingCost, d.subtotal, d.quantity)"
            + " FROM OrderDetail d WHERE d.order.orderTime BETWEEN ?1 AND ?2")
    public List<OrderDetail> findWithCategoryAndTimeBetween(Date startTime, Date startEnd);
    
    @Query("SELECT NEW com.happyshop.common.entity.order.OrderDetail(d.quantity, d.product.name,"
            + " d.productCost, d.shippingCost, d.subtotal)"
            + " FROM OrderDetail d WHERE d.order.orderTime BETWEEN ?1 AND ?2")
    public List<OrderDetail> findWithProductAndTimeBetween(Date startTime, Date startEnd);
}
