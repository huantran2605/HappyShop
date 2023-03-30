package com.happyshop.question.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.question.QuestionLike;
import com.happyshop.common.entity.question.Question;

@Repository
public interface QuestionLikeRepository extends JpaRepository<QuestionLike, Integer>{
        
    public QuestionLike findByCustomerAndQuestion(Customer customer, Question question);
}
