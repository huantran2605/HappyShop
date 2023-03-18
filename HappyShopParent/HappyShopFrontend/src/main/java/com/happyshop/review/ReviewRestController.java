package com.happyshop.review;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.CustomerUtility;
import com.happyshop.Utility;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.product.Product;
import com.happyshop.customer.CustomerService;
import com.happyshop.order.OrderService;
import com.happyshop.product.ProductService;

@RestController
public class ReviewRestController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ProductService productService;
    @Autowired
    CustomerUtility customerUtility;
    
    
    
    @PostMapping("/review/check_review_existed")
    private int checkReviewExisted(@RequestParam("productId") Integer productId,
            HttpServletRequest request) {
        
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        Product product = productService.findById(productId).get();
        int reviewId = reviewService.checkCustomerHasReviewForProduct(customer, product);
              
        return reviewId;
    }
    
    
    
}
