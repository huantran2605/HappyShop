package com.happyshop.question.reply;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    
    @Query("SELECT r FROM Reply r WHERE r.approvalStatus = false AND"
            + " CONCAT(r.reply_content,' ',r.replyTime) LIKE %?1%")          
    public Page<Reply> findAllNotApproved(String keyWord, Pageable pageable);  
    
    @Query("SELECT r FROM Reply r WHERE r.adminReplyRequired = true AND"
            + " CONCAT(r.reply_content,' ',r.replyTime) LIKE %?1%") 
    public Page<Reply> findAllAdminReplyRequired(String keyWord, Pageable pageable);
    
    @Query("SELECT r FROM Reply r WHERE r.approvalStatus = false ")          
    public Page<Reply> findAllNotApproved(Pageable pageable);  
    
    @Query("SELECT q FROM Reply q WHERE q.adminReplyRequired = true ") 
    public Page<Reply> findAllAdminReplyRequired(Pageable pageable);
    
    
    
    
    
}
