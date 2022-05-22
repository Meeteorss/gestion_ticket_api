package school.spring.school.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//import lombok.RequiredArgsConstructor;
import school.spring.school.filters.CorsFilter;
import school.spring.school.filters.CustomAuthFilter;
import school.spring.school.filters.CustomAuthorizationFilter;
import school.spring.school.handlers.CustomLogoutHandler;
import school.spring.school.models.User.Role;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	 @Autowired
	 private CustomLogoutHandler logoutHandler;

	public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.cors().configurationSource(request ->{
			var cors = new CorsConfiguration();
			cors.setAllowedOrigins(List.of("http://localhost:3000"));
		      cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
		      cors.setAllowedHeaders(List.of("*"));
		      cors.setAllowCredentials(true);
		      return cors;
		});
		http.csrf().disable();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/user/regsiter/**", "/auth/refresh/**").permitAll();
		http.authorizeRequests().antMatchers("/client/**").hasAnyAuthority(Role.CLIENT.toString(),
				Role.ADMIN.toString());
		http.authorizeRequests().antMatchers("/dev/**").hasAnyAuthority(Role.DEV.toString(), Role.ADMIN.toString());
		http.authorizeRequests().antMatchers("/admin/**").hasAnyAuthority(Role.ADMIN.toString());
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new CustomAuthFilter(authenticationManagerBean()));
		http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(CorsFilter(), SessionManagementFilter.class);
		http.logout().logoutSuccessHandler(logoutHandler).deleteCookies("refresh_token").invalidateHttpSession(true).clearAuthentication(true).deleteCookies("refresh_token");
		
	}

	@Bean
	CorsFilter CorsFilter() {
		CorsFilter filter = new CorsFilter();
		return filter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}

	/*
	 * @Bean CorsConfigurationSource corsConfigurationSource() { CorsConfiguration
	 * configuration = new CorsConfiguration();
	 * configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
	 * configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT"));
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
	 * configuration); return source; }
	 */
}
