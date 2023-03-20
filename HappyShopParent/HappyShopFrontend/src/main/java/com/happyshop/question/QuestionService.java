package com.happyshop.question;

import java.util.List;
import java.util.Optional;

import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.product.Product;

public interface QuestionService {
    List<Question> getMostRecentQuestionOfProduct(Product product);
    
    Optional<Question> findById(Integer id);
    
    <S extends Question> S save(S entity);
}
