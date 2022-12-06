package com.happyshop.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.happyshop.common.entity.AuthenticationType;
import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.Customer;

public interface CustomerService {

    List<Country> getAllCountries();

    Customer findByVerificationCode(String verificationCode);

    Customer findByEmail(String email);
    
    boolean checkUniqueEmail(String email);
    
    void encodePassword(Customer customer);
    
    void createCustomer(Customer customer);
    
    <S extends Customer> S save(S entity);
    
    boolean verifyAccount(String code);
    
    void updateAuthenticationType(Customer customer, AuthenticationType type);

    void addNewCustomerOAuth2(String name, String email, String countryCode);
    
    void setName(String name, Customer customer);
}
