
package com.happyshop.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.Role;
import com.happyshop.common.entity.User;

public class CustomerDetailsClass implements UserDetails {
	
	private Customer customer;

	public CustomerDetailsClass(Customer customer) {
		this.customer = customer;
	}

	@Override  
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return customer.isEnabled();
	}
	public String getFullname () {
		return customer.getFirstName() +" " +customer.getLastName();
	}
	
	public Customer getCustomer() {
	    return this.customer;
	}
	
}
