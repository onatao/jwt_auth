package com.devnatao.crud.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devnatao.crud.model.UserDataDetail;
import com.devnatao.crud.model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthencationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static final int TOKEN_EXPIRATION = 600000;
	public static final String TOKEN_SIGN = "token";

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		try {
			// Converte o conteúdo do JSON em UserModel.class
			UserModel user = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);
			
			/*
			 *  AuthenticationManager irá fazer a valiação, recebendo o login e senha do JSON convertido
			 *  em classe, e como terceiro parâmetro  uma lista de permissões.
			 */
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							user.getLogin(),
							user.getPassword(),
							new ArrayList<>()
							)
					);
		
		} catch (IOException e) {
			throw new RuntimeException("Falha", e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		/*
		 * É necessário fazer o typecast porque .getPrincipal() é um objeto
		 * e não classe genérica.
		 */
		UserDataDetail userData = (UserDataDetail) authResult.getPrincipal();
		
		// geração do token jwt utiação lizando o auth0
		String token = JWT.create()
					.withSubject(userData.getUsername()) // nome do usuário
					.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION)) // tempo de expiração do token (ms atuais + data de exp)
					.sign(Algorithm.HMAC512(TOKEN_SIGN)); // assina o token
		// registrar o token
		response.getWriter().write(token);
		response.getWriter().flush();
	}
	
	
	
	
}
