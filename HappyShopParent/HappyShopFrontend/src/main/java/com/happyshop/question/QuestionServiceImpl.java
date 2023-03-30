package com.happyshop.question;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.exception.QuestionNotFoundException;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionRepository repo;
    
    public List<Question> getMostRecentQuestionOfProduct(Product product) {
        List<Question> questions = repo.findByProductAndApprovalStatus(product);
        
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
    
       
    
    public List<Question> findByProductAndApprovalStatus(Product product) {
        return repo.findByProductAndApprovalStatus(product);
    }


    public Optional<Question> findById(Integer id) {
        return repo.findById(id);
    }



    public <S extends Question> S save(S entity) {
        return repo.save(entity);
    }



    public Page<Question> findByProductAndApprovalStatus(Product product, Pageable pageable) {
        return repo.findByProductAndApprovalStatus(product, pageable);
    }
    
    
}
