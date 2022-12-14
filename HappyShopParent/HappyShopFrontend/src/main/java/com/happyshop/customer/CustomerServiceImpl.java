package com.happyshop.customer;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.happyshop.Utility;
import com.happyshop.common.entity.AuthenticationType;
import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.exception.CustomerException;
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
    public Customer findByResetPasswordToken(String resetPasswordToken) {
        return customerRepository.findByResetPasswordToken(resetPasswordToken);
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
    public void addNewCustomerOAuth2(String name, String email, String countryCode, AuthenticationType authenticationType) {
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
        customer.setAuthenticationType(authenticationType);
        
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
            String firstName = customerName[0];
            customer.setFirstName(customerName[0]);
            String lastName = name.replace(firstName + " ","");
            customer.setLastName(lastName);           
        }
    }
    
    @Override
    public void updateCustomer(Customer customer) {
        Optional<Customer> oldCustomer = customerRepository.findById(customer.getId());    
        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            customer.setPassword(oldCustomer.get().getPassword());
        } else {
            encodePassword(customer);
        }
        customer.setCreatedTime(oldCustomer.get().getCreatedTime());
        customer.setVerificationCode(oldCustomer.get().getVerificationCode());
        customer.setEnabled(oldCustomer.get().isEnabled());
        customer.setAuthenticationType(oldCustomer.get().getAuthenticationType());
        customer.setResetPasswordToken(oldCustomer.get().getResetPasswordToken());
        
        customerRepository.save(customer);
    }
    
    @Override
    public String updateResetPasswordToken(String email) throws CustomerException {
        Customer customer = customerRepository.findByEmail(email);
        if(customer != null) {
            String token = RandomString.make(30);
            customer.setResetPasswordToken(token);
            return token;
        }
        else {
            throw new CustomerException("The email is not existed.");
        }
    }
    
    @Override
    public void resetPasswordCustomer(String token, String password) throws CustomerException {
        Customer customer = customerRepository.findByResetPasswordToken(token);
        if(customer != null) {
            customer.setPassword(password);
            encodePassword(customer);
            customer.setResetPasswordToken(null);
            customerRepository.save(customer);
        }
        else {
            throw new CustomerException("Could not find the user.");
        }
    }
    
    public Customer getAuthenticationCustomer(HttpServletRequest request) {
        String email = Utility.getEmailAuthenticationCustomer(request);
        if(email == null) {
            return null;
        }
        Customer customer = customerRepository.findByEmail(email);
        return customer;
    }

    
}
