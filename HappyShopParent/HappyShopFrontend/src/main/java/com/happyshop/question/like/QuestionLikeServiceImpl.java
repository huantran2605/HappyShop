package com.happyshop.question.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.question.QuestionLike;
import com.happyshop.common.entity.question.Question;

@Service
public class QuestionLikeServiceImpl implements QuestionLikeService {
    @Autowired
    private QuestionLikeRepository repo;
    
    
    public void handleLikeQuestionMethod(Customer customer, Question question){
        QuestionLike qL = new QuestionLike(customer, question);
        
        //set total like of that question +=1
        question.increaseLikeCount();
        repo.save(qL);
    }
    
    
    
    public void handleUnLikeQuestionMethod(Customer customer, Question question){
        QuestionLike qL = repo.findByCustomerAndQuestion(customer, question);
        //set total like of that question -=1
        question.decreaseLikeCount();
        repo.delete(qL);
        
    }

    
    public QuestionLike findByCustomerAndQuestion(Customer customer, Question question) {
        return repo.findByCustomerAndQuestion(customer, question);
    }
    
  
}
