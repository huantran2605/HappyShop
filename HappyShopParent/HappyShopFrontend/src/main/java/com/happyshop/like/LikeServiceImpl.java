package com.happyshop.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.Like;
import com.happyshop.common.entity.Question;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeRepository repo;
    
    public void handleLikeReviewMethod(Customer customer, Review review){
        Like rL = new Like(customer, review);
        
        //set total like of that review +=1
        review.increaseLikeCount();
        repo.save(rL);
    }
    
    public void handleLikeQuestionMethod(Customer customer, Question question){
        Like qL = new Like(customer, question);
        
        //set total like of that question +=1
        question.increaseLikeCount();
        repo.save(qL);
    }
    
    
    public void handleUnLikeReviewMethod(Customer customer, Review review){
        Like rL = repo.findByCustomerAndReview(customer, review);
        //set total like of that review -=1
        review.decreaseLikeCount();
        repo.delete(rL);
        
    }
    
    public void handleUnLikeQuestionMethod(Customer customer, Question question){
        Like qL = repo.findByCustomerAndQuestion(customer, question);
        //set total like of that question -=1
        question.decreaseLikeCount();
        repo.delete(qL);
        
    }

    public Like findByCustomerAndReview(Customer customer, Review review) {
        return repo.findByCustomerAndReview(customer, review);
    }
    
    public Like findByCustomerAndQuestion(Customer customer, Question question) {
        return repo.findByCustomerAndQuestion(customer, question);
    }
    
  
}
