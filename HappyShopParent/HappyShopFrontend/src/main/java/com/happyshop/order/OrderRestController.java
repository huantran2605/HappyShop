package com.happyshop.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.CustomerUtility;
import com.happyshop.Utility;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.exception.CustomerNotFoundException;
import com.happyshop.common.exception.OrderNotFoundException;
import com.happyshop.customer.CustomerService;

@RestController
public class OrderRestController {
    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;
    @Autowired
    private CustomerUtility customerUtility;
    
    @PostMapping("/orders/return")
    public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest,
            HttpServletRequest servletRequest) {
        
        Customer customer = null;
        
        customer =  customerUtility.getAuthenticationCustomer(servletRequest);
        
        try {
            orderService.setOrderReturnRequested(returnRequest, customer);
        } catch (OrderNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(returnRequest.getOrderId(), HttpStatus.OK);
    }
    
   
}
