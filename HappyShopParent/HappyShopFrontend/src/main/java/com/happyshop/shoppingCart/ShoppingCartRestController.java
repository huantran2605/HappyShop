package com.happyshop.shoppingCart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.Utility;
import com.happyshop.common.entity.Customer;
import com.happyshop.customer.CustomerService;

@RestController
public class ShoppingCartRestController {
    @Autowired
    CustomerService customerService;
    @Autowired
    CartItemService cartItemService;
    
    @PostMapping("/cart/add_to_cart/{productId}/{quantity}")
    public String addToCart(@PathVariable("productId") Integer productId,
            @PathVariable("quantity") Integer quantity,
           HttpServletRequest request) {
        String email = Utility.getEmailAuthenticationCustomer(request);
        if(email == null) {
            return "must login";
        }               
        Customer customer = customerService.findByEmail(email);
        cartItemService.addProduct(quantity, productId, customer);       
        return "added the cart";
    }
    
    @PostMapping("/cart/update_quantity/{productId}/{quantity}")
    public String updateQuantity(@PathVariable("productId") Integer productId,
            @PathVariable("quantity") Integer quantity,
           HttpServletRequest request) {
        String email = Utility.getEmailAuthenticationCustomer(request);
        if(email == null) {
            return "must login";
        }  
        Customer customer = customerService.findByEmail(email);
        float newSubTotal = cartItemService.updateQuantity(quantity, customer, productId);       
        return String.valueOf(newSubTotal);
    }
}
