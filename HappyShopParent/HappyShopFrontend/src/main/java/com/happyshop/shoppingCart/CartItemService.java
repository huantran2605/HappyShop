package com.happyshop.shoppingCart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;

public interface CartItemService {
    void addProduct(Integer quantity, Integer productId, Customer customer);
    
    List<CartItem> findByCustomer(Customer customer);
    
    float updateQuantity(Integer quantity, Customer customer, Integer productId);
    
    void deleteByCustomerAndProduct(Customer cutomer, Integer productId) ;
    
    Customer getAuthenticationCustomer(HttpServletRequest request);
}
