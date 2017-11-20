package com.helmo.archi.google.googleuse;

import com.helmo.archi.google.googleuse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootApplication
public class GoogleUseApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GoogleUseApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GoogleUseApplication.class);
	}
	
	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repo) throws Exception {
		builder.userDetailsService(new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
				return new CustomUserDetails(repo.findByEmail(s));
			}
		});
	}
}
