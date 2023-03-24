package com.happyshop.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.exception.QuestionNotFoundException;

public interface QuestionService {
    
    int SIZE_PAGE_QUESTION = 10;
    
    Page<Question> findAllNotApproved(String keyword, Pageable pageable);
    
    Page<Question> findAllNotAnswered(String keyword, Pageable pageable);
    
    Page<Question> findAllNotApprovedAndNotAnswered(String keyword, Pageable pageable);
    
    void deleteById(Integer id) throws QuestionNotFoundException;
      
    Question findById(Integer id) throws QuestionNotFoundException;
    
    <S extends Question> S save(S entity);
}
