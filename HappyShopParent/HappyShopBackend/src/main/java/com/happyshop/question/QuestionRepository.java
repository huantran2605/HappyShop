package com.happyshop.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    
    @Query("SELECT q FROM Question q WHERE "
            + " CONCAT(q.customer.firstName,' ',q.customer.lastName,' ', q.question_content,' ',q.product.name) LIKE %?1% ")          
    public Page<Question> findAll(String keyword, Pageable pageable);  
    
    
}
