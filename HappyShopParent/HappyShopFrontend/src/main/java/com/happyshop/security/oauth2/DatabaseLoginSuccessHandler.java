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
import com.happyshop.security.CustomerDetailsClass;
import com.happyshop.setting.country.CountryService;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        CustomerDetailsClass customerDetail = (CustomerDetailsClass) authentication.getPrincipal();
        Customer customer = customerDetail.getCustomer();
        customerService.updateAuthenticationType(customer, AuthenticationType.DATABASE);

        String redirectUrl = request.getParameter("redirect");
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            setDefaultTargetUrl(redirectUrl); // Set the default redirect URL to the value of the "redirect" parameter
        } else {
            setDefaultTargetUrl("/"); // Set the default redirect URL to "/"
        }

        super.onAuthenticationSuccess(request, response, authentication);

    }

}
