package com.happyshop.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.happyshop.Utility;
import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.Customer;
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
}
