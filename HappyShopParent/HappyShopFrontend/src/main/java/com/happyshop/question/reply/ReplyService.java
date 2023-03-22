package com.happyshop.question.reply;

import java.util.List;

import com.happyshop.common.entity.Reply;

public interface ReplyService {
    <S extends Reply> S save(S entity);
    
    List<Reply> findByQuestionAndApprovalStatus(Integer questionId);
}
