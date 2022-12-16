package com.happyshop.address;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.Customer;


@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{
    public List<Address> findByCustomer(Customer customer);
    
    @Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    public Address findByIdAndCustomer(Integer addressId, Integer customerId);
    
    @Query("DELETE FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    @Modifying
    @Transactional
    public void deleteByIdAndCustomer(Integer addressId, Integer customerId);   
    
    @Query("UPDATE Address a SET a.defaultForShipping = true where a.id = ?1")
    @Modifying
    @Transactional
    public void setDefaultAddress(Integer addressId);
    
    @Query("UPDATE Address a SET a.defaultForShipping = false where a.id != ?1 and a.customer.id = ?2")
    @Modifying
    @Transactional
    public void setNonDefaultAddress(Integer addressId, Integer customerId); 
      
    @Query("select a from Address a where a.customer.id = ?1 and a.defaultForShipping = true")
    public Address findByDefaultAddress(Integer customerId);
    
}
