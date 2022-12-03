package com.happyshop.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.Customer;

public interface CustomerService {
    
    int SIZE_PAGE_CUSTOMER = 10;

    List<Customer> findAll();
    
    Page<Customer> findAll(Pageable pageable, String keyword);
    
    Optional<Customer> findById(Integer id);
    
    String updateEnabledStatus(Customer customer);
    
    void deleteById(Integer id);
    
    String checkUniqueEmail(Integer id,String email);
    
    void updateCustomer(Customer customer);
}
