package com.happyshop.shoppingCart;

import java.util.List;

import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;

public interface CartItemService {
    void addProduct(Integer quantity, Integer productId, Customer customer);
    
    List<CartItem> findByCustomer(Customer customer);
}
