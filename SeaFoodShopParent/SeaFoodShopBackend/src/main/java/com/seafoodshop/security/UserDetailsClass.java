package com.seafoodshop.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.seafoodshop.common.entity.Role;
import com.seafoodshop.common.entity.User;

public class UserDetailsClass implements UserDetails {
	
	private User user;

	public UserDetailsClass(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> listAuth = new ArrayList<>();
		
		Set<Role> roles = user.getRoles();
		
		for (Role role : roles) {
			listAuth.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return listAuth;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
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
		return user.isEnable();
	}
	public String getFullname () {
		return user.getFirstName() +" " +user.getLastName();
	}
}
