package com.happyshop.common.entity.article;

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
@Table(name = "question_likes")
public class ArticleLike extends LikeAbstract { 
    @ManyToOne
    @JoinColumn(name="article_id")
    private Article article;    

    public ArticleLike(Customer customer, Article article) {
        this.article = article;
        this.customer = customer;
    }   
}
