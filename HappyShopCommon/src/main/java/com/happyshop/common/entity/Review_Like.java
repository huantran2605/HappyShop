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
@Table(name = "review_likes")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Review_Like { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name="review_id")
    private Review review;

    public Review_Like(Customer customer, Review review) {
        this.customer = customer;
        this.review = review;
    }
    
    
    
}
