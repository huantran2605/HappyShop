package com.happyshop.common.entity.question;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.reply.Reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name="questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Column(nullable = false,length = 300)
    private String content;
    
    @Column(name = "ask_time", nullable = false)
    private Date askTime;
    
    @Column(name = "answer_status", nullable = false)
    private boolean answerStatus;//need the admin or saleperson answer this question
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private QuestionVisitor visitor;
    
    @Column(name = "approval_status", nullable = false)
    private boolean approvalStatus;
        
    @Column
    private int likes;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionLike> likeStatus  = new ArrayList<>();

    public Question(Integer id) {
        this.id = id;
    }
    
    public void increaseLikeCount() {
        this.likes += 1;
    }

    public void decreaseLikeCount() {
        this.likes -= 1;
    }
    
   
}
