package com.seafoodshop.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.seafoodshop.user.UserService;

@RestController
@RequestMapping("user")
public class UserRestController {
	
	@Autowired
	UserService userService;
	@PostMapping("/check_email")
	public String checkEmailDuplicate(@Param("email") String email){
		return userService.hasEmailDb(email) ? "Duplicated" : "OK";
		
	}
	
}
