package com.happyshop;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.happyshop.admin.AmazonS3Util;
import com.happyshop.user.UserService;

@Controller
public class MainController {
    @Autowired
    UserService userS;
    
	@GetMapping("")
	private String viewHomePage() {
		return "index";
	}
	@GetMapping("/login")
	private String viewLoginPage() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if(auth == null || auth instanceof AnonymousAuthenticationToken) {
	        return "login";
	    }
	    return "redirect:/";
	}
	
}
