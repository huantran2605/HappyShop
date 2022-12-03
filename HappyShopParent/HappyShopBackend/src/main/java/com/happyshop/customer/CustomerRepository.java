package com.happyshop.customer;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    @Query("SELECT u FROM Customer u WHERE"
            + " CONCAT(u.id,' ',u.firstName,' ', u.lastName,' ',u.email,' ',u.phoneNumber"
            + ",' ',u.addressLine1,' ',u.addressLine2,' ',u.city,' ',u.state"
            + ",' ',u.postalCode,' ',u.country.name) LIKE %?1%")
    public Page<Customer> searchCustomer (String keyWord, Pageable pageable); 
    
    public Customer findByEmail(String email);
    
}
