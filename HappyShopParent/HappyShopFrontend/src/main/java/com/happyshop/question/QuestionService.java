package com.happyshop.question;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.product.Product;

public interface QuestionService {
    int SIZE_PAGE_QUESTION = 10;
    
    List<Question> getMostRecentQuestionOfProduct(Product product);
    
    Optional<Question> findById(Integer id);
    
    <S extends Question> S save(S entity);
    
    List<Question> findByProductAndApprovalStatus(Product product);
    
    Page<Question> findByProductAndApprovalStatus(Product product, Pageable pageable);
}
