package com.happyshop.question.like;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.question.QuestionLike;
import com.happyshop.common.entity.question.Question;

public interface QuestionLikeService {
    
    void handleLikeQuestionMethod(Customer customer, Question question);
    
    void handleUnLikeQuestionMethod(Customer customer, Question question);
    
    QuestionLike findByCustomerAndQuestion(Customer customer, Question question);
}
