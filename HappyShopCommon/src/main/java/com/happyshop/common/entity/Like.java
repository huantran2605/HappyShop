package com.happyshop.common.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Like { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name="review_id")
    private Review review;
    
    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;    

    public Like(Customer customer, Review review) {
        this.customer = customer;
        this.review = review;
    }
    
    public Like(Customer customer, Question question) {
        this.customer = customer;
        this.question = question;
    }
    
    
    
}
