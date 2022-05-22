package school.spring.school.filters;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;


import school.spring.school.repositories.UserRepository;
import school.spring.school.services.UserService;

public class CustomAuthFilter extends UsernamePasswordAuthenticationFilter {
	@Autowired
	UserService us = new UserService(null) ;
	private final AuthenticationManager authenticationManager;

	public CustomAuthFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		ObjectMapper objectMapper = new ObjectMapper();
		String JSONBody;

		try {
			JSONBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			LoginInput body = objectMapper.readValue(JSONBody, LoginInput.class);
			System.out.println("Request: " + body.toString());
			/*
			 * String username = request.getParameter("username"); String password =
			 * request.getParameter("password");
			 */
			String username = body.getUsername();
			String password = body.getPassword();
			System.out.println("Username: " + username + " password: " + password);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
			return authenticationManager.authenticate(authToken);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		User user = (User) authentication.getPrincipal();
		System.out.println("here "+user.getUsername());
		
		Algorithm alg = Algorithm.HMAC256("jeoaizjjqqd".getBytes());
		String access_token = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				
				.sign(alg);
		String refresh_token = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 7 * 60 * 60 * 1000))
				.withIssuer(request.getRequestURI().toString()).sign(alg);
		/*
		 * response.setHeader("access_token", access_token);
		 * response.setHeader("refresh_token", refresh_token);
		 */

		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", access_token);
		tokens.put("user", user.getUsername());
		Cookie cookie = new Cookie("refresh_token", refresh_token);
		cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}

}


class LoginInput {
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginInput [username=" + username + ", password=" + password + "]";
	}

}
