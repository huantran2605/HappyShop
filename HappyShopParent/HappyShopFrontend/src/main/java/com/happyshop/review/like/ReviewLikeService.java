package com.happyshop.review.like;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.review.Review;
import com.happyshop.common.entity.review.ReviewLike;

public interface ReviewLikeService {
    void handleLikeReviewMethod(Customer customer, Review review);
    
    void handleUnLikeReviewMethod(Customer customer, Review review);
    
    ReviewLike findByCustomerAndReview(Customer customer, Review review);
    
}
