package com.happyshop.question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.product.Product;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    
    @Query("SELECT q FROM Question q WHERE q.product = ?1 ORDER BY q.askTime DESC")
    public List<Question> findByProduct(Product product);     
        
    
}
