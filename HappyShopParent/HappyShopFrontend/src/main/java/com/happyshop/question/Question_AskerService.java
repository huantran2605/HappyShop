package com.happyshop.question;

import com.happyshop.common.entity.Question_Asker;

public interface Question_AskerService {
    public <S extends Question_Asker> S save(S entity);
}
