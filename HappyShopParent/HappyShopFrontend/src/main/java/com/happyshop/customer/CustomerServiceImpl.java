package com.happyshop.customer;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.Customer;
import com.happyshop.setting.country.CountryService;

import net.bytebuddy.utility.RandomString;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CountryService countryService;
    @Autowired
    PasswordEncoder passwordEncoder;
    
    
    @Override
    public <S extends Customer> S save(S entity) {
        return customerRepository.save(entity);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer findByVerificationCode(String verificationCode) {
        return customerRepository.findByVerificationCode(verificationCode);
    }
    
    @Override
    public List<Country> getAllCountries() {
        return countryService.findAllByOrderByNameAsc();
    }
    
    @Override
    public boolean checkUniqueEmail(String email) {
         Customer customer =  customerRepository.findByEmail(email);
         if(customer != null) {
             return false;
         }
         return true;
    }
    @Override
    public void createCustomer(Customer customer) {
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        String code = RandomString.make(64);
        customer.setVerificationCode(code);
        System.out.println(code);

    }
    
    @Override
    public void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }
    
    @Override
    public boolean verifyAccount(String code) {
        Customer customer = customerRepository.findByVerificationCode(code);
        if(customer != null && customer.isEnabled() == false) {
            customer.setEnabled(true);
            customer.setVerificationCode(null);
            customerRepository.save(customer);
            return true;
        }
        
        return false;
    }
  
}
