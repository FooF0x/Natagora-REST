package com.helmo.archi.google.googleuse.security;

import com.helmo.archi.google.googleuse.model.Role;
import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserRepository usrRepo;
	
	@Autowired
	private PasswordEncoder passEnc;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		User usr = usrRepo.findByEmail(name);
		
		if (usr != null && passEnc.matches(password, usr.getPassword())) {
			
			List<GrantedAuthority> auths = new ArrayList<>();
			for(Role role : usr.getRoles()) {
				auths.add(new SimpleGrantedAuthority(role.getName()));
			}
			
			// use the credentials
			// and authenticate against the third-party system
			return new UsernamePasswordAuthenticationToken(
					name, password, auths);
		} else {
			throw new UsernameNotFoundException("User doesn't exist");
		}
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(
				UsernamePasswordAuthenticationToken.class);
	}
}
