package com.seafoodshop;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.seafoodshop.user.UserService;

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
		return "login";
	}
}
