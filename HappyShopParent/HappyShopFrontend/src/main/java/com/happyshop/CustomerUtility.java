package com.happyshop;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.happyshop.common.entity.Customer;
import com.happyshop.customer.CustomerService;

@Component
public class CustomerUtility {
    @Autowired
    CustomerService customerService;
    
    public Customer getAuthenticationCustomer(HttpServletRequest request) {
        String email = Utility.getEmailAuthenticationCustomer(request);
        if(email == null) {
            return null;
        }
        Customer customer = customerService.findByEmail(email);
        return customer;
    }
}
