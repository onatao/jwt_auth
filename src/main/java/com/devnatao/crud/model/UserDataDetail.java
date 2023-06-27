package com.devnatao.crud.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDataDetail implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private Optional<UserModel> user;

	public UserDataDetail(Optional<UserModel> user2) {
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	public String getPassword() {
		/*
		 *  Retornará a senha do usuário, ou um usuário
		 *  vazio com senha também em vazio - .orElse
		 */
		return user.orElse(new UserModel()).getPassword();
	}

	@Override
	public String getUsername() {
		/*
		 *  Retornará o login do usuário, ou um usuário
		 *  vazio com login também em vazio - .orElse
		 */
		return user.orElse(new UserModel()).getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
