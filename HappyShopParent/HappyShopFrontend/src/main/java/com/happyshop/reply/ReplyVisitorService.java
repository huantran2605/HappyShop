package com.happyshop.reply;

import com.happyshop.common.entity.reply.ReplyVisitor;

public interface ReplyVisitorService {
    <S extends ReplyVisitor> S save(S entity);
}
