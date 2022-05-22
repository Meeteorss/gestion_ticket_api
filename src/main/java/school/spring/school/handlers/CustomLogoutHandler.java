package school.spring.school.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;
@Service
public class CustomLogoutHandler implements LogoutSuccessHandler {


	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		response.setStatus(201);
		/*Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		for (Cookie c : cookies) {
			if (c.getName().equals("refresh_token")) {
				cookie = c;
			}
		}*/
		Cookie cookie = new Cookie("refresh_token", "");
		
		cookie.setMaxAge(-1); 
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		
		
	}

}
