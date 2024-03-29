package com.happyshop.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.Customer;

public interface AddressService {
    List<Address> findByCustomer(Customer customer);    
    
    <S extends Address> S save(S entity);
    
    Address findByIdAndCustomer(Integer addressId, Integer customerId);
    
    void deleteByIdAndCustomer(Integer addressId, Integer customerId);
    
    void setDefaultAddress(Integer addressId, Integer customerId);
    
    Address findByDefaultAddress(Integer customerId);
    
}
