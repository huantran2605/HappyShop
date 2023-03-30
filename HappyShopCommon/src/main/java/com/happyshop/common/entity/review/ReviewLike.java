package com.happyshop.common.entity.review;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.abstractEntity.LikeAbstract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "review_likes")
public class ReviewLike extends LikeAbstract { 
    
    @ManyToOne
    @JoinColumn(name="review_id")
    private Review review;    

    public ReviewLike(Customer customer, Review review) {
        this.review = review;
        this.customer = customer;
    }   
}
