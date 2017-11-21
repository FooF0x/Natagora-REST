//package com.helmo.archi.google.googleuse.configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{
//
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//				.antMatchers("/").permitAll() //Everyone is allowed to see this page
//				.antMatchers("/users/**").authenticated(); //Only authenticated users will have access to /users and above
//	}
//}
