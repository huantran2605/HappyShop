package com.happyshop.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.product.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 128)
    private String headline;
    @Column(nullable = false, length = 500)
    private String comment;
    @Column(nullable = false)
    private int rating;
    @Column(nullable = false)
    private Date reviewTime;
    
    @Column(nullable = false)  
    private int likes;
    
    
    @ManyToOne
    @JoinColumn(name ="customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    public Review(String headline, String comment, int rating, Date reviewTime, Customer customer, Product product) {
        this.headline = headline;
        this.comment = comment;
        this.rating = rating;
        this.reviewTime = reviewTime;
        this.customer = customer;
        this.product = product;
    }
    
    
    
    public void increaseLikeCount() {
        this.likes += 1;
    }

    public void decreaseLikeCount() {
        this.likes -= 1;
    }

    public Review(Integer id) {
        this.id = id;
    }
    
    
}
