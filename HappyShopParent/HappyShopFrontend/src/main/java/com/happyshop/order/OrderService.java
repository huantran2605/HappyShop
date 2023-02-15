package com.happyshop.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.checkout.CheckoutInfo;
import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.order.PaymentMethod;
import com.happyshop.common.exception.OrderNotFoundException;

public interface OrderService {
    int SIZE_PAGE_ORDER = 5;
    
    Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
            PaymentMethod paymentMethod, CheckoutInfo checkoutInfo);
    
    Page<Order> findAll(String keyword, Integer customerId, Pageable pageable);
    
    Order findByOrderIdAndCustomer(Integer orderId, Integer customerId);
    
    void setOrderReturnRequested(OrderReturnRequest request, Customer customer) 
            throws OrderNotFoundException;
    
}
