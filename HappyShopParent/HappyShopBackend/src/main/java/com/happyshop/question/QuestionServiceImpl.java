package com.happyshop.question;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.entity.review.Review;
import com.happyshop.common.exception.QuestionNotFoundException;
import com.happyshop.common.exception.ReviewNotFoundException;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionRepository repo;

        
    public Page<Question> findAllNotApproved(String keyword, Pageable pageable) {
        if(!keyword.isBlank() || !keyword.isEmpty()) {
            return repo.findAllNotApproved(keyword, pageable);                      
        }
        return repo.findAllNotApproved(pageable);
    }
    
    public Page<Question> findAllNotAnswered(String keyword, Pageable pageable) {
        if(!keyword.isBlank() || !keyword.isEmpty()) {
            return repo.findAllNotAnswered(keyword, pageable);                      
        }
        return repo.findAllNotAnswered(pageable);
    }
    
    public Page<Question> findAllNotApprovedAndNotAnswered(String keyword, Pageable pageable) {
        if(!keyword.isBlank() || !keyword.isEmpty()) {
            return repo.findAllNotApprovedAndNotAnswered(keyword, pageable);                      
        }
        return repo.findAllNotApprovedAndNotAnswered(pageable);
    }
    
    
    public void deleteById(Integer id) throws QuestionNotFoundException {
        try {
            repo.deleteById(id);                     
        } catch (Exception e) {
            throw new QuestionNotFoundException("The question is not existed!");
        }
    }

    public Question findById(Integer id) throws QuestionNotFoundException {  
        try {
            return repo.findById(id).get();                       
        } catch (Exception e) {
            throw new QuestionNotFoundException("The question is not existed!");
        }
    }

    public <S extends Question> S save(S entity) {
        return repo.save(entity);
    }
    
    
    

}
