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

import com.happyshop.security.oauth2.CustomerOauth2UserService;
import com.happyshop.security.oauth2.DatabaseLoginSuccessHandler;
import com.happyshop.security.oauth2.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig {
    
    @Autowired private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	@Autowired private  CustomerOauth2UserService oauth2UserService;
	@Autowired private DatabaseLoginSuccessHandler dbLoginSuccessHandler;
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
    
    @Bean
    public UserDetailsService userDetailsService() {  
        return new CustomerDetailsServiceClass();
    }
    
    
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		    .antMatchers("/customer/customer_details", "/customer/update", "/cart","/address_book/**",
		            "/order/**","/review/**",
		            "/checkout/**").authenticated()
		    .anyRequest().permitAll()
		    .and()
            .formLogin()
                .loginPage("/login")
                .successHandler(dbLoginSuccessHandler)
                .usernameParameter("email")
                .permitAll()                
            .and()
            .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)
            .and()
            .logout().permitAll()
            .and()
            .rememberMe()
                .key("hfgeurhgefsdfsfsbgb1236744jh2345er")
                .tokenValiditySeconds( 14 * 24 * 60 * 60);

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer customizer() throws Exception {
		return (web) -> web.ignoring().antMatchers("/images/**","/css/**","/js/**");
	}

	
	
}
