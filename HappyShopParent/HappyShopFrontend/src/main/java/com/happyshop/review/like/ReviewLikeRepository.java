package com.happyshop.review.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.question.QuestionLike;
import com.happyshop.common.entity.review.Review;
import com.happyshop.common.entity.review.ReviewLike;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Integer>{
    
    public ReviewLike findByCustomerAndReview(Customer customer, Review review);
    
}
