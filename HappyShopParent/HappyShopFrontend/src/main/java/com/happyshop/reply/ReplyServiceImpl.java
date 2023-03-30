package com.happyshop.reply;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.entity.question.QuestionVisitor;
import com.happyshop.common.entity.reply.Reply;
import com.happyshop.common.exception.QuestionNotFoundException;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    ReplyRepository repo;
    
    public <S extends Reply> S save(S entity) {
        return repo.save(entity);
    }

    public List<Reply> findByQuestionAndApprovalStatus(Integer questionId) {
        return repo.findByQuestionAndApprovalStatus(questionId);
    }
    
    
    
    
    
    
    
    
    
    

}
