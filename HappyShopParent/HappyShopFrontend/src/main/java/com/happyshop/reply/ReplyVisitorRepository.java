package com.happyshop.reply;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.entity.question.QuestionVisitor;
import com.happyshop.common.entity.reply.ReplyVisitor;

@Repository
public interface ReplyVisitorRepository extends JpaRepository<ReplyVisitor, Integer> {
    
   
}
