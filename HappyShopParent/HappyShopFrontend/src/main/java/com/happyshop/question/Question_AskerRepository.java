package com.happyshop.question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Question_Asker;
import com.happyshop.common.entity.product.Product;

@Repository
public interface Question_AskerRepository extends JpaRepository<Question_Asker, Integer> {
    
   
}
