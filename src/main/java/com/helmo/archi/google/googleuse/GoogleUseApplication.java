package com.helmo.archi.google.googleuse;

import com.helmo.archi.google.googleuse.model.Role;
import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.RoleRepository;
import com.helmo.archi.google.googleuse.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
	public ApplicationRunner startApp(UserRepository usrRepo, RoleRepository roleRepo, PasswordEncoder passEnc, Environment env) {
		return args -> {
			
			checkRolesIntegrity(roleRepo);
			
			User admUsr; //TODO Improve
			if((admUsr = usrRepo.findOne(1L)) == null)
				admUsr = usrRepo.findByEmail(env.getProperty("user.admin.email"));
			if(!checkAdminIntegrity(admUsr, env, passEnc, roleRepo))
				if (admUsr != null)
					usrRepo.delete(admUsr);
			
			if(usrRepo.findOne(1L) == null) {
				usrRepo.save(new User(
						env.getProperty("user.admin.name"),
						env.getProperty("user.admin.email"),
						passEnc.encode(env.getProperty("user.admin.password")),
						true,
						env.getProperty("helmo.storage.defaultPic.onlineLocation"),
						Arrays.asList(
								roleRepo.findOneByName("ROLE_ADMIN"),
								roleRepo.findOneByName("ROLE_SYSTEM"),
								roleRepo.findOneByName("ROLE_USER"))
				));
			}
			
			User sysUsr;
			if((sysUsr  = usrRepo.findOne(2L)) == null)
				sysUsr = usrRepo.findByEmail(env.getProperty("user.system.email"));
			if(!checkSystemIntegrity(sysUsr, env, passEnc, roleRepo))
				if(sysUsr != null)
					usrRepo.delete(admUsr);
			
			if(usrRepo.findOne(2L) == null) {
				usrRepo.save(new User(
						env.getProperty("user.system.name"),
						env.getProperty("user.system.email"),
						passEnc.encode(env.getProperty("user.system.password")),
						false,
						env.getProperty("helmo.storage.defaultPic.onlineLocation"),
						Arrays.asList(
								roleRepo.findOneByName("ROLE_SYSTEM"),
								roleRepo.findOneByName("ROLE_USER"))
				));
			}
			
			if(usrRepo.findByEmail("user@nat.be") == null) { //TODO Remove for production
				usrRepo.save(new User(
						"user",
						"user@nat.be",
						passEnc.encode("useruser"),
						false,
						env.getProperty("helmo.storage.defaultPic.onlineLocation"),
						Arrays.asList(
								roleRepo.findOneByName("ROLE_USER"))
				));
			}
			
		};
	}
	
	private boolean checkAdminIntegrity(User usr, Environment env, PasswordEncoder passEnc, RoleRepository roleRepo) {
		return usr != null
				&& usr.getId() == 1L
				&& usr.getFullName().equals(env.getProperty("user.admin.name"))
				&& usr.getEmail().equals(env.getProperty("user.admin.email"))
				&& passEnc.matches(env.getProperty("user.admin.email"), usr.getPassword())
				&& usr.getOnlinePath().equals(env.getProperty("helmo.storage.defaultPic.onlineLocation"))
				&& usr.isAdmin()
				&& usr.getRoles().contains(roleRepo.findOneByName("ROLE_ADMIN"))
				&& usr.getRoles().contains(roleRepo.findOneByName("ROLE_SYSTEM"))
				&& usr.getRoles().contains(roleRepo.findOneByName("ROLE_USER"));
	}
	
	private boolean checkSystemIntegrity(User usr, Environment env, PasswordEncoder passEnc, RoleRepository roleRepo) {
		return usr != null
				&& usr.getId() == 2L
				&& usr.getFullName().equals(env.getProperty("user.system.name"))
				&& usr.getEmail().equals(env.getProperty("user.system.email"))
				&& passEnc.matches(env.getProperty("user.system.email"), usr.getPassword())
				&& usr.getOnlinePath().equals(env.getProperty("helmo.storage.defaultPic.onlineLocation"))
				&& !usr.isAdmin()
				&& usr.getRoles().contains(roleRepo.findOneByName("ROLE_SYSTEM"))
				&& usr.getRoles().contains(roleRepo.findOneByName("ROLE_USER"));
				
	}
	
	private void checkRolesIntegrity(RoleRepository roleRepo) {
		List<Role> roles = new ArrayList<>();
		if(roleRepo.findOneByName("ROLE_ADMIN") == null) {
			Role adm = new Role();
			adm.setName("ROLE_ADMIN");
			adm.setDescription("Natagora's Administrator");
			roles.add(adm);
		}
		if(roleRepo.findOneByName("ROLE_SYSTEM") == null) {
			Role sys = new Role();
			sys.setName("ROLE_SYSTEM");
			sys.setDescription("Allow a non human user to do some actions");
			roles.add(sys);
		}
		if(roleRepo.findOneByName("ROLE_USER") == null) {
			Role usr = new Role();
			usr.setName("ROLE_USER");
			usr.setDescription("Simple user");
			roles.add(usr);
		}
		roleRepo.save(roles);
	}
}
