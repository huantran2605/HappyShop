package com.happyshop.review.review_like;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.CustomerUtility;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.Review_Like;
import com.happyshop.common.exception.ReviewNotFoundException;
import com.happyshop.review.ReviewService;

@RestController
public class ReviewLikeRestController {
    @Autowired
    ReviewLikeService reviewLikeService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    CustomerUtility customerUtility;
    
    @PostMapping("/review/like")
    public String likeReview(HttpServletRequest request, 
            @RequestParam("reviewId") Integer reviewId) throws ReviewNotFoundException {
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        Review review = reviewService.findById(reviewId);
        if(review == null) {
            return "the review is not existed!";
        }
        reviewLikeService.handleLikeReviewMethod(customer, review); 
        return "ok";
    }
    
    
    @PostMapping("/review/unlike")
    public String unlikeReview(HttpServletRequest request, @RequestParam("reviewId") Integer reviewId) throws ReviewNotFoundException {
        Review review = reviewService.findById(reviewId);
        if(review == null) {
            return "the review is not existed!";
        }
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        
        reviewLikeService.handleUnLikeReviewMethod(customer, review);
        
        return "ok";
        
    }
    
    @PostMapping("/review/like_check")
    public String checkCustomerLikeReview(HttpServletRequest request,
            @RequestParam("reviewId") Integer reviewId) throws ReviewNotFoundException {
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        Review review = reviewService.findById(reviewId);
        if(review == null) {
            return "the review is not existed!";
        }
        Review_Like rL = reviewLikeService.findByCustomerAndReview(customer, review);
        if(rL != null) {
            return "liked";
        }
        else {
            return "didn't like";
        }
    }
   
}
