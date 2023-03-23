package com.happyshop.like;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Like;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Review;

public interface LikeService {
    void handleLikeReviewMethod(Customer customer, Review review);
    
    void handleUnLikeReviewMethod(Customer customer, Review review);
    
    Like findByCustomerAndReview(Customer customer, Review review);
    
    void handleLikeQuestionMethod(Customer customer, Question question);
    
    void handleUnLikeQuestionMethod(Customer customer, Question question);
    
    Like findByCustomerAndQuestion(Customer customer, Question question);
}
