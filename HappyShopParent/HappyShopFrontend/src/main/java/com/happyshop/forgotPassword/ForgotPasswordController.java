package com.happyshop.forgotPassword;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.happyshop.Utility;
import com.happyshop.common.exception.CustomerException;
import com.happyshop.customer.CustomerService;
import com.happyshop.setting.EmailSettingBag;
import com.happyshop.setting.SettingService;

@Controller
public class ForgotPasswordController {
    @Autowired CustomerService customerService;
    @Autowired SettingService settingService;
    
    @GetMapping("/forgot_password")
    public String forgotPasswordForm() {
        return "customer/forgot_password_form";
    }
    
    @PostMapping("/forgot_password")
    public String processResquestForm(HttpServletRequest request, Model model) {
        String email =  request.getParameter("email");
        try {
            String token = customerService.updateResetPasswordToken(email);
            String link = Utility.getSiteUrl(request) + "/customer/reset_password?token=" + token;        
            sendEmail(link, email);
            model.addAttribute("message", "We have sent the link to change your password to your email.");  
        }catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Could not send message.");      
        } catch (CustomerException e) {
            model.addAttribute("error", e.getMessage());
        }
        
        return "customer/forgot_password_form";
    }
    
    public void sendEmail(String link, String email) throws UnsupportedEncodingException, MessagingException {
        EmailSettingBag emailSettings =  settingService.getEmailSetting();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
        
        String toAddress = email;
        String subject = "Link to reset your password";
        String content = "<p> Hello,</p>"
                + "<p>You have requested reset your password.</p>"
                + "<p>Click the link below to reset your password.</p>"
                + "<p> <a href= \"" + link + "\">Reset your password</a></p>"
                + "<p>Ignore this email if you already have reseted your password or you do not this request.</p>";
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
             
        helper.setText(content, true);
        mailSender.send(message);           
    }
}
