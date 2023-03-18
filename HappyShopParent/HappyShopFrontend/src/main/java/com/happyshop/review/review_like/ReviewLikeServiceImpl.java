package com.happyshop.review.review_like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.Review_Like;

@Service
public class ReviewLikeServiceImpl implements ReviewLikeService {
    @Autowired
    private ReviewLikeRepository repo;
    
    public void handleLikeReviewMethod(Customer customer, Review review){
        Review_Like rL = new Review_Like(customer, review);
        
        //set total like of that review +=1
        review.increaseLikeCount();
        repo.save(rL);
    }
    
    public void handleUnLikeReviewMethod(Customer customer, Review review){
        Review_Like rL = repo.findByCustomerAndReview(customer, review);
        //set total like of that review -=1
        review.decreaseLikeCount();
        repo.delete(rL);
        
    }

    public Review_Like findByCustomerAndReview(Customer customer, Review review) {
        return repo.findByCustomerAndReview(customer, review);
    }
    
    
  
}
