//package com.helmo.archi.google.googleuse.configuration;
//
//import com.helmo.archi.google.googleuse.model.Role;
//import com.helmo.archi.google.googleuse.model.User;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class CustomUserDetails implements UserDetails {
//
//	private String username;
//	private String password;
//	private Collection<? extends GrantedAuthority> authorities;
//
//	public CustomUserDetails(User byEmail) {
//		this.username = byEmail.getEmail();
//		this.password = byEmail.getPassword();
//
//		this.authorities = translate(byEmail.getRoles());
//	}
//
//	private Collection<? extends GrantedAuthority> translate(List<Role> roles) {
//		List<GrantedAuthority> authorities = new ArrayList<>();
//		for (Role role : roles) {
//			String name = role.getName().toUpperCase();
//			//Make sure that all roles start with "ROLE_"
//			if (!name.startsWith("ROLE_"))
//				name = "ROLE_" + name;
//			authorities.add(new SimpleGrantedAuthority(name));
//		}
//		return authorities;
//	}
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return authorities;
//	}
//
//	@Override
//	public String getPassword() {
//		return password;
//	}
//
//	@Override
//	public String getUsername() {
//		return username;
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return false;
//	}
//}
