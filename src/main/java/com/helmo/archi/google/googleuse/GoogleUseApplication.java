package com.helmo.archi.google.googleuse;

//import com.helmo.archi.google.googleuse.configuration.CustomUserDetails;
import com.helmo.archi.google.googleuse.model.Role;
import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import java.util.Arrays;

@SpringBootApplication
public class GoogleUseApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GoogleUseApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GoogleUseApplication.class);
	}
	
//	@Autowired
//	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repo) throws Exception {
//		if(repo.count() == 0)
//			repo.save(new User(
//					"Quentin Grignet",
//					"user",
//					"user",
//					Arrays.asList(
//							new Role("USER"),
//							new Role("PROVIDER")
//					)));
//		builder.userDetailsService(s -> new CustomUserDetails(repo.findByEmail(s)));
//	}
}
