package com.happyshop.question.reply;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Question_Asker;
import com.happyshop.common.entity.Reply_Person;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.exception.QuestionNotFoundException;

@Service
public class Reply_PersonServiceImpl implements  Reply_PersonService {
    @Autowired
    Reply_PersonRepository repo;
    
    public <S extends Reply_Person> S save(S entity) {
        return repo.save(entity);
    }
    
    
    
    
    
    
    
    

}
