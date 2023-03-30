package com.happyshop.question.visitor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.entity.question.QuestionVisitor;
import com.happyshop.common.exception.QuestionNotFoundException;

@Service
public class QuestionVisitorServiceImpl implements QuestionVisitorService {
    @Autowired
    QuestionVisitorRepository repo;
    
    public <S extends QuestionVisitor> S save(S entity) {
        return repo.save(entity);
    }
    
    
    
    
    
    
    
    

}
