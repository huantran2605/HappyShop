package com.happyshop.question.reply;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Reply;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.exception.QuestionNotFoundException;

public interface ReplyService {
    
    int SIZE_PAGE_REPLY = 10;
    
    Page<Reply> findAllNotApproved(String keyword, Pageable pageable);
    
    Page<Reply> findAllAdminReplyRequired(String keyword, Pageable pageable);
}
