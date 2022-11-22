package com.happyshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceClass();
	}
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/user/**").hasAuthority("Admin")
			.antMatchers("/category/**").hasAnyAuthority("Admin","Editor")
			.antMatchers("/brand/**").hasAnyAuthority("Admin","Editor")
			
			.antMatchers("/product/new", "/product/delete/**").hasAnyAuthority("Admin", "Editor")
            
            .antMatchers("/product/update/**", "/product/saveOrUpdate", "/product/updateEnabled/**")
                .hasAnyAuthority("Admin", "Editor", "Salesperson")
                
            .antMatchers("/product/listProduct", "/product/detail/**", "/product/page/**")
                .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                
            .antMatchers("/product/**").hasAnyAuthority("Admin", "Editor")
            .antMatchers("/product/detail/**", "/customers/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")
			.anyRequest().authenticated() 
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.permitAll()
			.and()
			.logout().permitAll()
			.and()
			.rememberMe()
				.key("hfgeurhgebgb1236744jh2345er")
				.tokenValiditySeconds( 5 * 24 * 60 * 60);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer customizer() throws Exception {
		return (web) -> web.ignoring().antMatchers("/images/**","/css/**","/js/**");
	}

	
	
}
