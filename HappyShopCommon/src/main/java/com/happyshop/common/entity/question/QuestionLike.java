package com.happyshop.common.entity.question;

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
public class QuestionLike extends LikeAbstract { 
    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;    

    public QuestionLike(Customer customer, Question question) {
        this.question = question;
        this.customer = customer;
    }   
}
