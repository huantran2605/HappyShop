package com.happyshop.common.entity.abstractEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.entity.question.QuestionLike;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@AllArgsConstructor
@Data 
@NoArgsConstructor
public class LikeAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    
    @ManyToOne
    @JoinColumn(name="customer_id")
    protected Customer customer;
}
