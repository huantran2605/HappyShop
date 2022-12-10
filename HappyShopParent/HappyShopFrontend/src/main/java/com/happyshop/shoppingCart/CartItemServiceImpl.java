package com.happyshop.shoppingCart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;

@Service
public class CartItemServiceImpl implements CartItemService{
    @Autowired
    CartItemRepository cartItemRepo;
    
    public void addProduct(Integer quantity, Integer productId, Customer customer) {
        CartItem cartItem =  cartItemRepo.findByCustomerAndProduct(customer, productId);
        int updateQuantity = quantity;
        if(cartItem != null) {
            updateQuantity = quantity + cartItem.getQuantity();
            cartItem.setQuantity(updateQuantity);
            cartItemRepo.save(cartItem);
        }
        else {
            Product product = new Product(productId);
            CartItem newCartItem = new CartItem(customer, product, updateQuantity);
            cartItemRepo.save(newCartItem);
        }
    }

    public List<CartItem> findByCustomer(Customer customer) {
        return cartItemRepo.findByCustomer(customer);
    }
    
    
    
}
