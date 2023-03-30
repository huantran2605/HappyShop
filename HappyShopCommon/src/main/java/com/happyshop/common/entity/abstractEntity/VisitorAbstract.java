package com.happyshop.common.entity.abstractEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@AllArgsConstructor
@Data
@NoArgsConstructor
public class VisitorAbstract {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    
    @Column(name = "full_name", nullable = false, length = 30)
    protected String fullName;
    
    @Column(name = "phone_number")
    protected String phoneNumber;
    
    @Column
    protected String email;

    public VisitorAbstract(String fullName, String phoneNumber, String email) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    
    
}
