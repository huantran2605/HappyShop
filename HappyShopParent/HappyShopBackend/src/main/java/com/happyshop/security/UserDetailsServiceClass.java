package com.happyshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.happyshop.common.entity.User;
import com.happyshop.user.UserService;

public class UserDetailsServiceClass implements UserDetailsService {
	
	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByEmail(username);
		
		if(user != null) {
			return new UserDetailsClass(user);
		}
		throw new UsernameNotFoundException("Could not find the user with " + username);
	}

}
