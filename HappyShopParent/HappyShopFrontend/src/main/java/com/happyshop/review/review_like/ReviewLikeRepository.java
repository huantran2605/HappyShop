package com.happyshop.review.review_like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.Review_Like;

@Repository
public interface ReviewLikeRepository extends JpaRepository<Review_Like, Integer>{
    
    public Review_Like findByCustomerAndReview(Customer customer, Review review);

    
}
