package com.happyshop.review.like;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.CustomerUtility;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.question.QuestionLike;
import com.happyshop.common.entity.review.Review;
import com.happyshop.common.entity.review.ReviewLike;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.exception.ReviewNotFoundException;
import com.happyshop.question.QuestionService;
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
    public ResponseEntity<String> likeReview(HttpServletRequest request, 
            @RequestParam("reviewId") Integer reviewId) throws ReviewNotFoundException {
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        
        Review review = reviewService.findById(reviewId);
        if(review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found the review id " + reviewId);
        }            
        reviewLikeService.handleLikeReviewMethod(customer, review);             
        
        
        return ResponseEntity.ok("like review id " + reviewId +" successfully!");
    }
    
    
    @PostMapping("/review/unlike")
    public ResponseEntity<String> unlikeReview(HttpServletRequest request, 
            @RequestParam("reviewId") Integer reviewId) throws ReviewNotFoundException {
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        Review review = reviewService.findById(reviewId);            
        if(review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found review id" + reviewId);
        }
        reviewLikeService.handleUnLikeReviewMethod(customer, review);
        
        return ResponseEntity.ok("unlike review id " +reviewId +" successfully!");        
    }
    
    @PostMapping("/review/like_check")
    public ResponseEntity<String> checkCustomerLikeReview(
            @RequestParam("reviewId") Integer reviewId, 
            HttpServletRequest request) throws ReviewNotFoundException { 
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        
        Review review = reviewService.findById(reviewId);  
        if(review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found review id" + reviewId);
        }
        ReviewLike rL = reviewLikeService.findByCustomerAndReview(customer, review); 
        if(rL != null) {
            return ResponseEntity.ok("review id " + reviewId + " liked");
        }
        else {
            return ResponseEntity.ok("review id " + reviewId + " did not liked");
        }
            
    }
   
}
