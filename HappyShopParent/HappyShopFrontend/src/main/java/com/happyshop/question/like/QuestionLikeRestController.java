package com.happyshop.question.like;

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
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.exception.ReviewNotFoundException;
import com.happyshop.question.QuestionService;
import com.happyshop.review.ReviewService;

@RestController
public class QuestionLikeRestController {
    @Autowired
    QuestionLikeService questionLikeService;
    @Autowired
    CustomerUtility customerUtility;
    @Autowired
    QuestionService questionService;
    
    @PostMapping("/question/like")
    public ResponseEntity<String> likeReview(HttpServletRequest request, 
            @RequestParam("questionId") Integer questionId) throws ReviewNotFoundException {
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        
        Question question = questionService.findById(questionId).get();
        if(question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found the question id " + questionId);
        }
        questionLikeService.handleLikeQuestionMethod(customer, question);
        
        return ResponseEntity.ok("like question id " +questionId +" successfully!");
    }
    
    
    @PostMapping("/question/unlike")
    public ResponseEntity<String> unlikeReview(HttpServletRequest request, 
            @RequestParam("questionId") Integer questionId) throws ReviewNotFoundException {
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        
        Question question = questionService.findById(questionId).get();
        if(question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found the question id " + questionId);
        }
        questionLikeService.handleUnLikeQuestionMethod(customer, question);
        
        return ResponseEntity.ok("unlike question id " +questionId +" successfully!");        
    }
    
    @PostMapping("/question/like_check")
    public ResponseEntity<String> checkCustomerLikeReview(
            @RequestParam("questionId") Integer questionId, 
            HttpServletRequest request) throws ReviewNotFoundException { 
        System.out.println("question id " + questionId);
        Customer customer =  customerUtility.getAuthenticationCustomer(request);
        
        Question question = questionService.findById(questionId).get();
        if(question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found question id" + questionId);
        }
        QuestionLike qL = questionLikeService.findByCustomerAndQuestion(customer, question);
        if(qL != null) {
            return ResponseEntity.ok("question id " + questionId + " liked");
        }
        else {
            return ResponseEntity.ok("question id " + questionId + " did not liked");
        }
        
    }
   
}
