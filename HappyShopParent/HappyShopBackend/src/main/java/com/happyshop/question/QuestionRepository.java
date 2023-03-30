package com.happyshop.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.question.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    
    @Query("SELECT q FROM Question q WHERE q.approvalStatus = false AND q.answerStatus = true AND"
            + " CONCAT(q.question_content,' ',q.product.name,' ',"
            + " q.askTime) LIKE %?1%")          
    public Page<Question> findAllNotApproved(String keyWord, Pageable pageable);  
    
    @Query("SELECT q FROM Question q WHERE q.approvalStatus = false AND q.answerStatus = true ")
    public Page<Question> findAllNotApproved(Pageable pageable);
    
    @Query("SELECT q FROM Question q WHERE q.answerStatus = false AND q.approvalStatus = true  AND"
            + " CONCAT(q.question_content,' ',q.product.name,' ',"
            + " q.askTime) LIKE %?1%")           
    public Page<Question> findAllNotAnswered(String keyWord, Pageable pageable); 
    
    @Query("SELECT q FROM Question q WHERE q.answerStatus = false AND q.approvalStatus = true")
    public Page<Question> findAllNotAnswered(Pageable pageable);
    
    @Query("SELECT q FROM Question q WHERE q.approvalStatus = false AND q.answerStatus = false AND"
            + " CONCAT(q.question_content,' ',q.product.name,' ',"
            + " q.askTime) LIKE %?1%")
    public Page<Question> findAllNotApprovedAndNotAnswered(String keyWord, Pageable pageable);
    
    @Query("SELECT q FROM Question q WHERE q.answerStatus = false AND q.approvalStatus = false")
    public Page<Question> findAllNotApprovedAndNotAnswered(Pageable pageable);
    
    
}
