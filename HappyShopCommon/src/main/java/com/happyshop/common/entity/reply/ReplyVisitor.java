package com.happyshop.common.entity.reply;

import javax.persistence.Entity;

import javax.persistence.Table;

import com.happyshop.common.entity.abstractEntity.VisitorAbstract;


@Entity
@Table(name="reply_visitors")
public class ReplyVisitor extends VisitorAbstract {

    public ReplyVisitor(String fullName, String phoneNumber, String email) {
        super(fullName, phoneNumber, email);
    }  
}
