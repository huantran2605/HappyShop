package com.happyshop.common.entity.question;


import javax.persistence.Entity;
import javax.persistence.Table;

import com.happyshop.common.entity.abstractEntity.VisitorAbstract;



@Entity
@Table(name= "question_visitors")
public class QuestionVisitor extends VisitorAbstract {

    public QuestionVisitor(String fullName, String phoneNumber, String email) {
        super(fullName, phoneNumber, email);
    }
    public QuestionVisitor() {
        super();
    }
    
    
}
