package com.devnatao.crud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.devnatao.crud.service.UserDetailServiceImp;

public class JWTConfiguration extends WebSecurityConfiguration {
	
	@Autowired
	private UserDetailServiceImp userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*
		 * define UserDetailServiceImp e PasswordEncoder como classes
		 * base para o Spring Security
		 */
		auth
		.userDetailsService(userService) 
		.passwordEncoder(passwordEncoder);
	}
	
	 

}
