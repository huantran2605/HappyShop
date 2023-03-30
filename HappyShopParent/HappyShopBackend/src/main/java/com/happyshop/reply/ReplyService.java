package com.happyshop.reply;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.entity.reply.Reply;
import com.happyshop.common.exception.QuestionNotFoundException;

public interface ReplyService {
    
    int SIZE_PAGE_REPLY = 10;
    
    Page<Reply> findAllNotApproved(String keyword, Pageable pageable);
    
    Page<Reply> findAllAdminReplyRequired(String keyword, Pageable pageable);
    
    Optional<Reply> findById(Integer id);
    
    <S extends Reply> S save(S entity);
    
    void deleteById(Integer id);
}

