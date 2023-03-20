package com.happyshop.question;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.exception.QuestionNotFoundException;
import com.happyshop.common.exception.ReviewNotFoundException;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionRepository repo;
    
    public Page<Question> findAll(String keyword, Pageable pageable) {
        if(!keyword.isBlank() || !keyword.isEmpty()) {
            return repo.findAll(keyword, pageable);                      
        }
        return repo.findAll(pageable);
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
    
    

}
