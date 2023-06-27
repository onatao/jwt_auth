package com.devnatao.crud.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.devnatao.crud.model.UserDataDetail;
import com.devnatao.crud.model.UserModel;
import com.devnatao.crud.repository.UserRepository;

@Component
public class UserDetailServiceImp implements UserDetailsService	{

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserModel> user = repository.findUserByLogin(username);
		
		if (user.isEmpty()) 
			throw new UsernameNotFoundException("Usuário não encontrado!");
		
		return new UserDataDetail(user);
	}

}
