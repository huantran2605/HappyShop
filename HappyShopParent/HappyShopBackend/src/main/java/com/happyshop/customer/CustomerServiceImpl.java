package com.happyshop.customer;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.product.Product;
import com.happyshop.setting.country.CountryService;

import net.bytebuddy.utility.RandomString;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Override
    public List<Customer> findAll() {
        return customerRepo.findAll();
    }
    
    @Override
    public Page<Customer> findAll(Pageable pageable, String keyword) {
        if (!keyword.isBlank()) {
            return customerRepo.searchCustomer(keyword, pageable);
        }
        return customerRepo.findAll(pageable);
    }

    public Optional<Customer> findById(Integer id) {
        return customerRepo.findById(id);
    }
    
    @Override
    public String updateEnabledStatus(Customer customer) {
        String status = "";
        if (customer.isEnabled() == true) {
            customer.setEnabled(false);
            status = "Disable the customer id: " + customer.getId() + " successfully!";
        } else {
            customer.setEnabled(true);
            status = "Enable the customer id: " + customer.getId() + " successfully!";
        }
        customerRepo.save(customer);
        return status;
    }
    
    @Override
    public void deleteById(Integer id) {
        customerRepo.deleteById(id);
    }
    
    @Override
    public String checkUniqueEmail(Integer id,String email) {
        Customer customer =  customerRepo.findByEmail(email);
        if(customer != null) {
            if(customer.getId() ==  id) {
              return "Ok";
          }else {
              return "Duplicated";            
          }
        }
        return "Ok";
    }
    
    @Override
    public void updateCustomer(Customer customer) {
        Optional<Customer> oldCustomer = customerRepo.findById(customer.getId());             
        if(customer.getPassword() == null || customer.getPassword().isEmpty()) {
            customer.setPassword(oldCustomer.get().getPassword());
        }
        else {
            String encodedPass = passwordEncoder.encode(customer.getPassword());
            customer.setPassword(encodedPass);
        }        
        customer.setCreatedTime(oldCustomer.get().getCreatedTime());
        customer.setVerificationCode(oldCustomer.get().getVerificationCode());
        customer.setEnabled(oldCustomer.get().isEnabled());
        customer.setAuthenticationType(oldCustomer.get().getAuthenticationType());
        
        customerRepo.save(customer);
    }

   

  
}
