package com.happyshop.user.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;

import org.springframework.web.bind.annotation.PostMapping;


import org.springframework.web.bind.annotation.RestController;

import com.happyshop.user.UserService;

@RestController

public class UserRestController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/users/check_email")
	public String checkEmailUnique(Long id, String email){
		return userService.IsEmailUnique(id,email) ? "OK" : "Duplicated";		
	}
	
}
