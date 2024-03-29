package com.happyshop.security.oauth2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.authenticator.SavedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.happyshop.common.entity.AuthenticationType;
import com.happyshop.common.entity.Customer;
import com.happyshop.customer.CustomerRepository;
import com.happyshop.customer.CustomerService;
import com.happyshop.customer.CustomerServiceImpl;
import com.happyshop.setting.country.CountryService;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    
    @Autowired private CustomerService customerService;
        
    @Override 
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        
        CustomerOauth2User oauth2User = (CustomerOauth2User) authentication.getPrincipal();
        String name = oauth2User.getName();
        String email = oauth2User.getEmail();
        String countryCode = request.getLocale().getCountry();
        String clientName = oauth2User.getClientName();
        
        AuthenticationType authenticationType = getAuthenticationType(clientName);
        Customer customer = customerService.findByEmail(email);
        if(customer == null) {
            customerService.addNewCustomerOAuth2(name, email, countryCode, authenticationType);
        }else {
            customerService.updateAuthenticationType(customer, authenticationType);
            oauth2User.setFullName(customer.getFullName());
        }     
        
        String redirectUrl = request.getParameter("redirect");
        System.out.println(redirectUrl);
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            setDefaultTargetUrl(redirectUrl); // Set the default redirect URL to the value of the "redirect" parameter
        } else {
            setDefaultTargetUrl("/"); // Set the default redirect URL to "/"
        }
     
        super.onAuthenticationSuccess(request, response, authentication);
        
        
    }
    
    public AuthenticationType getAuthenticationType(String clientName) {
        if(clientName.equals("Google")) {
            return AuthenticationType.GOOGLE;
        }
        else {
            return AuthenticationType.FACEBOOK;

        }     
    }

}
