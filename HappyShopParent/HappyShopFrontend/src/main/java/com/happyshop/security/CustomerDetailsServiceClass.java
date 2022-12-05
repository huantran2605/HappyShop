package com.happyshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.happyshop.common.entity.Customer;
import com.happyshop.customer.CustomerService;

public class CustomerDetailsServiceClass implements UserDetailsService {
	
	@Autowired
	private CustomerService customerService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer customer = customerService.findByEmail(username);
		
		if(customer != null) {
			return new CustomerDetailsClass(customer);
		}
		throw new UsernameNotFoundException("Could not find the user with " + username);
	}

}
