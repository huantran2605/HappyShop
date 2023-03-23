package com.happyshop.like;

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
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.Like;
import com.happyshop.common.entity.Question;
import com.happyshop.common.exception.ReviewNotFoundException;
import com.happyshop.question.QuestionService;
import com.happyshop.review.ReviewService;

@RestController
public class LikeRestController {
    @Autowired
    LikeService likeService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    CustomerUtility customerUtility;
    @Autowired
    QuestionService questionService;
    
    @PostMapping("/like/{object}")
    public ResponseEntity<String> likeReview(HttpServletRequest request, 
            @PathVariable("object") String object,
            @RequestParam("objectId") Integer objectId) throws ReviewNotFoundException {
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        if(object.equals("review")) {
            Review review = reviewService.findById(objectId);
            if(review == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found the review id " + objectId);
            }            
            likeService.handleLikeReviewMethod(customer, review);             
        }
        else if(object.equals("question")) {
            Question question = questionService.findById(objectId).get();
            if(question == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found the question id " + objectId);
            }
            likeService.handleLikeQuestionMethod(customer, question);
        }
        return ResponseEntity.ok("like " + object + " id " +objectId +" successfully!");
    }
    
    
    @PostMapping("/unlike/{object}")
    public ResponseEntity<String> unlikeReview(HttpServletRequest request, 
            @PathVariable("object") String object,
            @RequestParam("objectId") Integer objectId) throws ReviewNotFoundException {
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        if(object.equals("review")) {
            Review review = reviewService.findById(objectId);            
            if(review == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found review id" + objectId);
            }
            likeService.handleUnLikeReviewMethod(customer, review);
        }
        else if(object.equals("question")){
            Question question = questionService.findById(objectId).get();
            if(question == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found the question id " + objectId);
            }
            likeService.handleUnLikeQuestionMethod(customer, question);
        }
        return ResponseEntity.ok("unlike " + object + " id " +objectId +" successfully!");        
    }
    
    @PostMapping("/like_check/{object}")
    public ResponseEntity<String> checkCustomerLikeReview(
            @PathVariable("object") String object,
            @RequestParam("objectId") Integer objectId, 
            HttpServletRequest request) throws ReviewNotFoundException { 
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        if(object.equals("review")) {
            Review review = reviewService.findById(objectId);  
            if(review == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found review id" + objectId);
            }
            Like rL = likeService.findByCustomerAndReview(customer, review);
            if(rL != null) {
                return ResponseEntity.ok(object + " " + objectId + " liked");
            }
            else {
                return ResponseEntity.ok(object + " " + objectId + " did not liked");
            }
            
        }
        else{
            Question question = questionService.findById(objectId).get();
            if(question == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found question id" + objectId);
            }
            Like rq = likeService.findByCustomerAndQuestion(customer, question);
            if(rq != null) {
                return ResponseEntity.ok(object + " " + objectId + " liked");
            }
            else {
                return ResponseEntity.ok(object + " " + objectId + " did not liked");
            }
        }
    }
   
}
