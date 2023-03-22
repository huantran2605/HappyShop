package com.happyshop.question.reply;

import com.happyshop.common.entity.Reply_Person;

public interface Reply_PersonService {
    <S extends Reply_Person> S save(S entity);
}
