package com.happyshop.shoppingCart;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.CustomerUtility;
import com.happyshop.Utility;
import com.happyshop.common.entity.Customer;
import com.happyshop.customer.CustomerService;

@RestController
public class ShoppingCartRestController {
    @Autowired
    CustomerService customerService;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    CustomerUtility customerUtility;
    
    @PostMapping("/cart/add_to_cart/{productId}/{quantity}")
    public String addToCart(@PathVariable("productId") Integer productId,
            @PathVariable("quantity") Integer quantity,
           HttpServletRequest request) {             
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        if(customer == null) {
            return "must login";
        }       
        cartItemService.addProduct(quantity, productId, customer);       
        return "added the cart";
    }
    
    @PostMapping("/cart/update_quantity/{productId}/{quantity}")
    public String updateQuantity(@PathVariable("productId") Integer productId,
            @PathVariable("quantity") Integer quantity,
           HttpServletRequest request) {
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        if(customer == null) {
            return "must login";
        }    
        float newSubTotal = cartItemService.updateQuantity(quantity, customer, productId);       
        return String.valueOf(newSubTotal);
    }
    
    @DeleteMapping("/cart/delete_item/{productId}")
    public String DeleteItems(@PathVariable("productId") Integer productId,
            HttpServletRequest request) {
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        if(customer == null) {
            return "must login";
        }        
        cartItemService.deleteByCustomerAndProduct(customer, productId);       
        return "deleted items";
    }
    
    
    
   
}
