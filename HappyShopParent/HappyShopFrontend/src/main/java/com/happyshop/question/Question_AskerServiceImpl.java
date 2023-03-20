package com.happyshop.question;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Question_Asker;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.exception.QuestionNotFoundException;

@Service
public class Question_AskerServiceImpl implements Question_AskerService {
    @Autowired
    Question_AskerRepository repo;
    
    public <S extends Question_Asker> S save(S entity) {
        return repo.save(entity);
    }
    
    
    
    
    
    
    
    

}
