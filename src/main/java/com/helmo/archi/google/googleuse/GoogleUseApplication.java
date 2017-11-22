package com.helmo.archi.google.googleuse;

import com.helmo.archi.google.googleuse.model.Password;
import com.helmo.archi.google.googleuse.model.Role;
import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.RoleRepository;
import com.helmo.archi.google.googleuse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.RoleResult;
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
	public ApplicationRunner startApp(UserRepository usrRepo, RoleRepository roleRepo, PasswordEncoder passEnc, Environment env) {
		return args -> {
			if(roleRepo.findOneByName("ROLE_ADMIN") == null) {
				Role adm = new Role();
				adm.setName("ROLE_ADMIN");
				adm.setDescription("Administrator");
				roleRepo.save(adm);
			}
			if(roleRepo.findOneByName("ROLE_SYSTEM") == null) {
				Role sys = new Role();
				sys.setName("ROLE_SYSTEM");
				sys.setDescription("System");
				roleRepo.save(sys);
			}
			if(roleRepo.findOneByName("ROLE_USER") == null) {
				Role usr = new Role();
				usr.setName("ROLE_USER");
				usr.setDescription("User");
				roleRepo.save(usr);
			}
			
			if(usrRepo.count() == 0) {
				User usr = new User();
				usr.setFullName("admin");
				usr.setEmail("admin@nat.be");
				usr.setPassword(passEnc.encode("rootroot"));
				usr.setAdmin(true);
				usr.setOnlinePath(env.getProperty("helmo.storage.defaultPic.onlineLocation"));
				
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
				usr2.setOnlinePath(env.getProperty("helmo.storage.defaultPic.onlineLocation"));
				List<Role> roles2 = new ArrayList<>();
				Role rl22 = new Role();
				rl22.setName("ROLE_USER");
				rl22.setDescription("User");
				roles2.add(rl2);
				usr2.setRoles(roles2);
				
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
