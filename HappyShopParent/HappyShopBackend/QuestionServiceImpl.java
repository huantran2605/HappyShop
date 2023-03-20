package com.happyshop.question;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.exception.QuestionNotFoundException;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionRepository repo;
    
    public List<Question> getMostRecentQuestionOfProduct(Product product) {
        List<Question> questions = repo.findByProduct(product);
        
        List<Question> recentQuestions = new ArrayList<>();
        
        int size = questions.size();
        int index = 0;
        while(size > 0) {
            recentQuestions.add(questions.get(index));
            index++;
            if(index == 3) {
                break;
            }
            size--;
        }
        
        return recentQuestions;
    }

    public Question findByProductAndCustomer(Integer productId, Integer customerId) {
        return repo.findByProductAndCustomer(productId, customerId);
    }
    
    
    
    
    
    
    

}
