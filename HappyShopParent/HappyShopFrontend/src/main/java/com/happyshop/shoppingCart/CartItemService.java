package com.happyshop.shoppingCart;

import com.happyshop.common.entity.Customer;

public interface CartItemService {
    void addProduct(Integer quantity, Integer productId, Customer customer);
}
