package com.happyshop.customer;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.AuthenticationType;
import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.Customer;
import com.happyshop.setting.country.CountryService;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CountryService countryService;
    
    
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
    public void updateAuthenticationType(Customer customer, AuthenticationType type) {
        if(!customer.getAuthenticationType().equals(type)) {
            customerRepository.updateAuthenticationType(customer.getId(), type);          
        }
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
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        String code = RandomString.make(64);
        customer.setVerificationCode(code);
        System.out.println(code);

    }
    
    @Override
    public void encodePassword(Customer customer) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(customer.getPassword());
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
    @Override
    public void addNewCustomerOAuth2(String name, String email, String countryCode) {
        Customer customer = new Customer();
        setName(name, customer);
        
        customer.setEmail(email);
        customer.setPassword("");
        customer.setPhoneNumber("");
        customer.setAddressLine1("");
        customer.setCity("");
        customer.setState("");
        customer.setPostalCode("");
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        
        Country country = countryService.findByCode(countryCode);
        customer.setCountry(country); 
        
        customerRepository.save(customer);
        
    }
  
    public void setName(String name, Customer customer) {
        String[] customerName = name.split(" ");
        if(customerName.length < 2) {
            customer.setFirstName(customerName[0]);    
            customer.setLastName(""); 
        }
        else {
            customer.setFirstName(customerName[0]);
            customer.setLastName(customerName[1]);           
        }
    }
    
}
