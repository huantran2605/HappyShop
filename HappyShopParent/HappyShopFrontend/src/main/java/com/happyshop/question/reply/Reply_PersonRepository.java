package com.happyshop.question.reply;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Question_Asker;
import com.happyshop.common.entity.Reply_Person;
import com.happyshop.common.entity.product.Product;

@Repository
public interface Reply_PersonRepository extends JpaRepository<Reply_Person, Integer> {
    
   
}
