package com.happyshop.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    
    public Customer findByEmail(String email);
    
    public Customer findByVerificationCode(String verificationCode);
    
    
}
