package com.happyshop;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.happyshop.common.entity.User;
import com.happyshop.user.UserService;

@Component
public class UserUtility {
    @Autowired
    UserService userService;
    
    public User getAuthenticationUser(HttpServletRequest request) {
        String email = getEmailAuthenticationUser(request);
        if(email == null) {
            return null;
        }
        User user = userService.findByEmail(email);
        return user;
    }
    
    public String getEmailAuthenticationUser(HttpServletRequest request) {
        Object principle =  request.getUserPrincipal();
        if(principle == null ) {
            return null;
        }
        String email = "";
        if(principle instanceof UsernamePasswordAuthenticationToken
                || principle instanceof RememberMeAuthenticationToken) {
            email = request.getUserPrincipal().getName();
        }   
        return email;
    }
}
