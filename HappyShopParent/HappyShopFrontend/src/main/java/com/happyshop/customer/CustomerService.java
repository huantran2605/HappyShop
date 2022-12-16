package com.happyshop.customer;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.happyshop.common.entity.AuthenticationType;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.setting.Country;
import com.happyshop.common.exception.CustomerException;

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

    void addNewCustomerOAuth2(String name, String email, String countryCode,AuthenticationType authenticationType);
    
    void setName(String name, Customer customer);
    
    void updateCustomer(Customer customer);

    String updateResetPasswordToken(String email) throws CustomerException;
    
    Customer findByResetPasswordToken(String resetPasswordToken);

    void resetPasswordCustomer(String token, String password) throws CustomerException;
    
    Customer getAuthenticationCustomer(HttpServletRequest request);
}
