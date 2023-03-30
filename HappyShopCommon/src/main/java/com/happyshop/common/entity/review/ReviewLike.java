package com.happyshop.common.entity.review;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.happyshop.common.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_likes")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ReviewLike { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name="review_id")
    private Review review;    
    
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    public ReviewLike(Customer customer, Review review) {
        this.review = review;
        this.customer = customer;
    }   
}
