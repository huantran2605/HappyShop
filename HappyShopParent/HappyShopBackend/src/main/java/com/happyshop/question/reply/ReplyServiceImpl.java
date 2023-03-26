package com.happyshop.question.reply;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Reply;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.exception.QuestionNotFoundException;
import com.happyshop.common.exception.ReviewNotFoundException;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    ReplyRepository repo;

        
    public Page<Reply> findAllNotApproved(String keyword, Pageable pageable) {
        if(!keyword.isBlank() || !keyword.isEmpty()) {
            return repo.findAllNotApproved(keyword, pageable);                      
        }
        return repo.findAllNotApproved(pageable);
    }
    
    public Page<Reply> findAllAdminReplyRequired(String keyword, Pageable pageable) {
        if(!keyword.isBlank() || !keyword.isEmpty()) {
            return repo.findAllAdminReplyRequired(keyword, pageable);                      
        }
        return repo.findAllAdminReplyRequired(pageable);
    }

    public Optional<Reply> findById(Integer id) {
        return repo.findById(id);
    }

    public <S extends Reply> S save(S entity) {
        return repo.save(entity);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
    
    
    
    

    
}
