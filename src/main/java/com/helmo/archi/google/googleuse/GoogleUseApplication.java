package com.helmo.archi.google.googleuse;

import com.helmo.archi.google.googleuse.model.Password;
import com.helmo.archi.google.googleuse.model.Role;
import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class GoogleUseApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GoogleUseApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GoogleUseApplication.class);
	}
	
	@Bean
	public PasswordEncoder encodage() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ApplicationRunner startApp(UserRepository usrRepo, PasswordEncoder passEnc) {
		return args -> {
			if(usrRepo.count() == 0) {
				User usr = new User();
				usr.setFullName("admin");
				usr.setEmail("admin@nat.be");
				usr.setPassword(passEnc.encode("rootroot"));
				usr.setAdmin(true);
				
				List<Role> roles = new ArrayList<>();
				Role rl1 = new Role();
				rl1.setName("ROLE_ADMIN");
				rl1.setDescription("Administrator");
				Role rl2 = new Role();
				rl2.setName("ROLE_USER");
				rl2.setDescription("User");
				roles.add(rl1);
				roles.add(rl2);
				
				usr.setRoles(roles);
				
				User usr2 = new User();
				usr2.setFullName("user");
				usr2.setEmail("user@nat.be");
				usr2.setPassword(passEnc.encode("useruser"));
				usr2.setAdmin(false);
				roles.remove(rl1);
				usr2.setRoles(roles);
				
				List<User> usrs = new ArrayList<>();
				usrs.add(usr);
				usrs.add(usr2);
				
				usrRepo.save(usrs);
			}
		};
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
