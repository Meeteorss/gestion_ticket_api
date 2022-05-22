package school.spring.school.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;


import static org.springframework.http.HttpStatus.FORBIDDEN;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


public class CustomAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getServletPath().equals("/login") || request.getServletPath().equals("/auth/refresh")) {
			filterChain.doFilter(request, response);
		} else {
			String authorizationHeader = request.getHeader(AUTHORIZATION);
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

				try {
					String token = authorizationHeader.substring("Bearer ".length());
					Algorithm alg = Algorithm.HMAC256("jeoaizjjqqd".getBytes());
					JWTVerifier verifier = JWT.require(alg).build();
					DecodedJWT decodedJWT = verifier.verify(token);
					String username = decodedJWT.getSubject();
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					stream(roles).forEach(role -> {
						authorities.add(new SimpleGrantedAuthority(role));

					});
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,
							null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authToken);
					filterChain.doFilter(request, response);
				} catch (Exception e) {
					System.out.println("Error logging in: " + e.getMessage());
					response.setHeader("error", e.getMessage());
					response.setStatus(FORBIDDEN.value());
					// response.sendError(FORBIDDEN.value());
					Map<String, String> error = new HashMap<>();
					error.put("error", e.getMessage());

					response.setContentType("application/json");
					new ObjectMapper().writeValue(response.getOutputStream(), error);
				}
			} else {
				Map<String, String> error = new HashMap<>();
				error.put("error", "No authorization header present in the request");

				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), error);
				filterChain.doFilter(request, response);
			}
		}
	}

}
