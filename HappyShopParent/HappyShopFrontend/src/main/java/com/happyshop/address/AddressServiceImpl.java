package com.happyshop.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.CustomerUtility;
import com.happyshop.Utility;
import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.Customer;
import com.happyshop.customer.CustomerService;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepository repo;
    
    @Autowired
    CustomerService customerService;
            
    public List<Address> findByCustomer(Customer customer) {
        return repo.findByCustomer(customer);
    }
    

    public <S extends Address> S save(S entity) {
        return repo.save(entity);
    }

    public Address findByIdAndCustomer(Integer addressId, Integer customerId) {
        return repo.findByIdAndCustomer(addressId, customerId);
    }

    public void deleteByIdAndCustomer(Integer addressId, Integer customerId) {
        repo.deleteByIdAndCustomer(addressId, customerId);
    }

    public void setDefaultAddress(Integer addressId, Integer customerId) {
        if(addressId > 0) {
            repo.setDefaultAddress(addressId);           
        }
        repo.setNonDefaultAddress(addressId, customerId);
    }

    public Address findByDefaultAddress(Integer customerId) {
        return repo.findByDefaultAddress(customerId);
    }
    
    

   
   
    
    
}
