package school.spring.school.controllers;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import school.spring.school.models.User;
import school.spring.school.models.User.Role;
import school.spring.school.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	@Autowired
	UserService us;

	@GetMapping("/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cookie cookie = null;
		try {
			Cookie[] cookies = request.getCookies();
			
			for (Cookie c : cookies) {
				if (c.getName().equals("refresh_token")) {
					cookie = c;
				}
			}
			if (cookie == null) {
				response.setHeader("error", "Cookie missing");
				response.setStatus(FORBIDDEN.value());
				// response.sendError(FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();
				error.put("error", "Cookie missing");

				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			} else {

				String refresh_token = cookie.getValue();
				System.out.println("Refresh token: " + refresh_token);
				Algorithm alg = Algorithm.HMAC256("jeoaizjjqqd".getBytes());
				JWTVerifier verifier = JWT.require(alg).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				User user = us.findByUsername(username);
				List<Role> roles = new ArrayList<>();
				roles.add(user.getRole());
				String access_token = JWT.create().withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", roles.stream().map(Role::toString).collect(Collectors.toList())).withClaim("userId", user.getId()).sign(alg);
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("user", user.getUsername());
				response.addCookie(cookie);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			
			}
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());

			response.setContentType("application/json");
			new ObjectMapper().writeValue(response.getOutputStream(), error);
			
		}finally {
			cookie = null;
		}

	}
	
	/*@GetMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response)throws IOException{
		
	}*/
}
