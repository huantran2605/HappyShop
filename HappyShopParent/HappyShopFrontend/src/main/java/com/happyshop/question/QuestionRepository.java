package com.happyshop.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.question.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    
    @Query("SELECT q FROM Question q WHERE q.product = ?1 AND q.approvalStatus = 1 ORDER BY q.askTime DESC")
    public List<Question> findByProductAndApprovalStatus(Product product);   
    
    @Query("SELECT q FROM Question q WHERE q.product = ?1 AND q.approvalStatus = 1")
    public Page<Question> findByProductAndApprovalStatus(Product product, Pageable pageable);   
        
    
}
