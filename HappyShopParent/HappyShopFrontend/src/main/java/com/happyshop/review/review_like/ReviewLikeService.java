package com.happyshop.review.review_like;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.Review_Like;

public interface ReviewLikeService {
    void handleLikeReviewMethod(Customer customer, Review review);
    
    void handleUnLikeReviewMethod(Customer customer, Review review);
    
    Review_Like findByCustomerAndReview(Customer customer, Review review);
}
