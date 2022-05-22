package school.spring.school.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

		((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers", "*");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Credentials", "true");
		((HttpServletResponse) response).setIntHeader("Access-Control-Max-Age", 180);
		chain.doFilter(request, response);

	}

}
