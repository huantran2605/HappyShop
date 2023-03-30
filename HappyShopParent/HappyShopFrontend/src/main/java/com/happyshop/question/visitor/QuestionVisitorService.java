package com.happyshop.question.visitor;

import com.happyshop.common.entity.question.QuestionVisitor;

public interface QuestionVisitorService {
    public <S extends QuestionVisitor> S save(S entity);
}
