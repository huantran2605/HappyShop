package com.happyshop.common.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.happyshop.common.entity.product.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name="replies")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 300, nullable = false)
    private String reply_content;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;  
    
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;
    
    @ManyToOne
    @JoinColumn(name = "person_reply_id")
    private Reply_Person personReply;
    
    @Column(name = "reply_time", nullable = false)
    private Date replyTime;
    
    @Column(name = "approval_status", nullable = false)
    private boolean approvalStatus;
    
    @ManyToOne
    @JoinColumn(name = "question_id")  
    private Question question;
    
    @Column(name = "admin_reply_required", nullable = false)
    private boolean adminReplyRequired;
       
       
}
