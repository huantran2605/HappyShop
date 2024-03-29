package com.happyshop.customer;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.Utility;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.setting.Country;
import com.happyshop.common.exception.CustomerNotFoundException;
import com.happyshop.security.CustomerDetailsClass;
import com.happyshop.security.oauth2.CustomerOauth2User;
import com.happyshop.setting.EmailSettingBag;
import com.happyshop.setting.SettingService;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    
    @Autowired
    SettingService settingService;
    
    @GetMapping("/register")
    public String register(Model model) {
        List<Country> listCountry = customerService.getAllCountries();
        model.addAttribute("listCountry", listCountry);
        model.addAttribute("customer",new Customer());
        
        return "customer/register_form";
    }
    
    @PostMapping("/create_account")
    public String createAccount(Model model, Customer customer, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        customerService.createCustomer(customer);
        sendVerifycationEmail(request, customer);
        model.addAttribute("titlePage", "Register successfully");
        customerService.save(customer);
        return "customer/register_success";
    }
    
    public void sendVerifycationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
        EmailSettingBag emailSettings =  settingService.getEmailSetting();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
        
        String toAddress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        
        content = content.replace("[[name]]", customer.getFullName());
        
        String verifyUrl = Utility.getSiteUrl(request) + 
                "/customer/verify?code=" + customer.getVerificationCode();
        
        content = content.replace("[[url]]", verifyUrl);
        
        helper.setText(content, true);
        mailSender.send(message);    
    }
    
    @GetMapping("/verify")
    public String verifyRegistration(@Param("code") String code) {
        boolean verified = customerService.verifyAccount(code);
        return "customer/"+(verified ? "verify_success" : "verify_fail");
    }
    
    @GetMapping("loginCustomer")
    public String loginC() {
        return "customer/loginForm";
    }
    
    @GetMapping("customer_details")
    public String viewCustomerDetails(HttpServletRequest request, Model model) {
        String email = Utility.getEmailAuthenticationCustomer(request);
        Customer customer =  customerService.findByEmail(email);
        List<Country> listCountry = customerService.getAllCountries();
        model.addAttribute("listCountry", listCountry);
        model.addAttribute("customer", customer);
        
        return "customer/customer_details";
    }
    
    
    
    @PostMapping("update")
    private String updateCustomer( Customer customer,
             RedirectAttributes re,HttpServletRequest request) {     
        customerService.updateCustomer(customer);    
        updateNameUserAuthentication(customer, request);
               
        String redirectOption = request.getParameter("redirect");
        String selectedProduct =  request.getParameter("selectedProduct");
        if(redirectOption.equals("address_book")) {
            re.addFlashAttribute("message", "Updated address successfully!");
            return "redirect:/address_book";
        }
        else if(redirectOption.equals("cart")) {
            return "redirect:/cart";
        }
        else if(redirectOption.equals("checkout")) {
            return "redirect:/address_book?redirect=checkout&selectedProduct=" + selectedProduct;   
        }
        re.addFlashAttribute("message", "Updated Profile successfully!");
               
        return "redirect:/customer/customer_details";
    }

    private void updateNameUserAuthentication(Customer customer, HttpServletRequest request) {
        Object principle =  request.getUserPrincipal();
        String fullName = customer.getFirstName() + " " + customer.getLastName();
          
        if(principle instanceof UsernamePasswordAuthenticationToken
                || principle instanceof RememberMeAuthenticationToken ) {
            CustomerDetailsClass customerDetail = getUserAuthenticationDatabase(principle);
            customerDetail.setFullName(fullName);
        }
        else if(principle instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principle;
            CustomerOauth2User oauth2User = (CustomerOauth2User) oauth2Token.getPrincipal();
            oauth2User.setFullName(fullName);
        }      
    }
    
    private CustomerDetailsClass getUserAuthenticationDatabase(Object principle) {
        if(principle instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken usernamePasswordToken = (UsernamePasswordAuthenticationToken) principle;
            return (CustomerDetailsClass) usernamePasswordToken.getPrincipal();
        }
        else {
            RememberMeAuthenticationToken usernamePasswordToken = (RememberMeAuthenticationToken) principle;
            return (CustomerDetailsClass) usernamePasswordToken.getPrincipal();
        }
            
    }
    
    @GetMapping("reset_password")
    public String showResetForm(@Param("token") String token, Model model) {
        Customer customer = customerService.findByResetPasswordToken(token);
        if(customer != null) {
            model.addAttribute("token", token);
            return "customer/reset_password_form";
        }
        else {
            model.addAttribute("message", "Invalid token");
            model.addAttribute("titlePage", "Invalid token");
            
            return "message";
        }
    }
    
    @PostMapping("reset_password")
    public String processResetForm(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
              
        try {
            customerService.resetPasswordCustomer(token, password);
            model.addAttribute("title", "Reset Your Password");
            model.addAttribute("message", "Reseted Your Password Successfully!");
            model.addAttribute("titlePage", "Reseted Your Password Successfully!");
            return "message";
        } catch (CustomerNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("titlePage", "Error");           
            return "message";
        }
    }
      
}
