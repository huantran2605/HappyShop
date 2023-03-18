package com.happyshop.shoppingCart;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.Utility;
import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;
import com.happyshop.customer.CustomerService;

@Service
public class CartItemServiceImpl implements CartItemService{
    @Autowired
    CartItemRepository cartItemRepo;
    
    @Autowired
    CustomerService  customerService;
    
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
        List<CartItem> list = cartItemRepo.findByCustomer(customer);
        for (CartItem cartItem : list) {
            if(cartItem.getQuantity() > cartItem.getProduct().getQuantity()) {
                cartItem.setQuantity(cartItem.getProduct().getQuantity());
            }
        }
        cartItemRepo.saveAll(list);
        return list;
    }

    public float updateQuantity(Integer quantity, Customer customer, Integer productId) {       
        cartItemRepo.updateQuantity(quantity, customer, productId);
        CartItem item = cartItemRepo.findByCustomerAndProduct(customer, productId);
        float newSubTotal = item.getQuantity() * item.getProduct().getDiscountPrice();        
        return newSubTotal;
    }

    public void deleteByCustomerAndProduct(Customer cutomer, Integer productId) {
        cartItemRepo.deleteByCustomerAndProduct(cutomer, productId);
    }
    

    public CartItem findByCustomerAndProduct(Customer customer, Integer productId) {
        return cartItemRepo.findByCustomerAndProduct(customer, productId);
    }

    
}
