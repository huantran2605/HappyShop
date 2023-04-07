package com.happyshop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    UserDetailsService userDetailsService() {
		return new UserDetailsServiceClass();
	}

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/user/**", "/setting/**", "/settings/**").hasAuthority("Admin")
                .antMatchers("/category/**").hasAnyAuthority("Admin", "Editor")
                .antMatchers("/brand/**").hasAnyAuthority("Admin", "Editor")

                .antMatchers("/product/new", "/product/delete/**","/article/**", "/article-topic/**").hasAnyAuthority("Admin", "Editor")

                .antMatchers("/product/update/**", "/product/saveOrUpdate", "/product/updateEnabled/**")
                .hasAnyAuthority("Admin", "Editor", "Salesperson")

                .antMatchers("/product/listProduct", "/product/detail/**", "/product/page/**")
                .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")

                .antMatchers("/product/**").hasAnyAuthority("Admin", "Editor")
                .antMatchers("/product/detail/**", "/customers/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")
                .antMatchers("/order", "/order/", "/order/page/**", "/order/listOrder", "/order/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
                .antMatchers("/customer/**", "/order/**", "/get_shipping_cost", "/reports/**", "/reply/**").hasAnyAuthority("Admin", "Salesperson")
                .antMatchers("/order_shippers/update/**").hasAnyAuthority("Shipper")
                .antMatchers("/review/**").hasAnyAuthority("Admin", "Assistant")
                .anyRequest().authenticated()
                .and()
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("email")
                        .permitAll())
                .logout(logout -> logout.permitAll())
                .rememberMe(me -> me
                        .key("hfgeurhgebgb1236744jh2345er")
                        .tokenValiditySeconds(5 * 24 * 60 * 60));
        http.headers(headers -> headers.frameOptions().sameOrigin());
		return http.build();
	}

    @Bean
    WebSecurityCustomizer customizer() throws Exception {
		return (web) -> web.ignoring().antMatchers("/images/**","/css/**","/js/**");
	}

	
	
}
