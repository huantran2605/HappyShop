package com.happyshop.question.reply;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Reply;
import com.happyshop.common.entity.product.Product;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer>{
    @Query("SELECT r FROM Reply r WHERE r.question.id = ?1 AND r.approvalStatus = 1 ORDER BY r.replyTime DESC")
    public List<Reply> findByQuestionAndApprovalStatus(Integer questionId);     
        
}
