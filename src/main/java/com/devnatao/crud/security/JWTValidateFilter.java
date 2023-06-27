package com.devnatao.crud.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTValidateFilter extends BasicAuthenticationFilter {

	public static final String HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public JWTValidateFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// requisitando o token
		String tokenWithBearer = request.getHeader(HEADER);
		
		if (tokenWithBearer == null) {
			chain.doFilter(request, response);
			return;
		}
		
		if (!tokenWithBearer.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		// removendo prefixo "Bearer " do token
		String tokenWithoutBearer = tokenWithBearer.replace(TOKEN_PREFIX, "");
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(tokenWithoutBearer);
		// SecurityContextHolder é aonde o spring guarda os dados do usuário já autenticado
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);
	}
	
	// Método que fará leitura do Token e retornará os dados do usuário
	private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
		// Irá retornar o nome do usuário (Subject)
		String user = JWT.require(
				Algorithm.HMAC512( // mesmo algorítimo utilizado na classe JWTAuthenticationFilter
						JWTAuthencationFilter.TOKEN_SIGN)) // TOKEN_SIGN criado na JWTAuthentcationFilter
				.build().
				verify(token) // Verifica o token recebido por parâmetro
				.getSubject(); // retorna o usuário (definido como subject no JWTAuthenticationFilter)
		
		if (user == null) return null;
		
		return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
	}
	
	
 
}
