package com.happyshop.review.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.review.Review;
import com.happyshop.common.entity.review.ReviewLike;

@Service
public class ReviewLikeServiceImpl implements ReviewLikeService {
    @Autowired
    private ReviewLikeRepository repo;
    
    public void handleLikeReviewMethod(Customer customer, Review review){
        ReviewLike rL = new ReviewLike(customer, review);
        
        //set total like of that review +=1
        review.increaseLikeCount();
        repo.save(rL);
    }
    
    
    public void handleUnLikeReviewMethod(Customer customer, Review review){
        ReviewLike rL = repo.findByCustomerAndReview(customer, review);
        //set total like of that review -=1
        review.decreaseLikeCount();
        repo.delete(rL);
        
    }
    

    public ReviewLike findByCustomerAndReview(Customer customer, Review review) {
        return repo.findByCustomerAndReview(customer, review);
    }
    
    
  
}
