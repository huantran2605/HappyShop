package com.happyshop.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.Like;
import com.happyshop.common.entity.Question;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer>{
    
    public Like findByCustomerAndReview(Customer customer, Review review);
    
    public Like findByCustomerAndQuestion(Customer customer, Question question);
}
