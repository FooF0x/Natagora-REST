package com.helmo.archi.google.googleuse;

import com.helmo.archi.google.googleuse.model.MediaType;
import com.helmo.archi.google.googleuse.model.Role;
import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.MediaTypeRepository;
import com.helmo.archi.google.googleuse.repository.RoleRepository;
import com.helmo.archi.google.googleuse.repository.UserRepository;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Paths;
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
	public PasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ApplicationRunner startApp(UserRepository usrRepo, RoleRepository roleRepo, MediaTypeRepository medRepo,
	                                  PasswordEncoder passEnc, Environment env, GoogleStorage storage) {
		return args -> {
			
			checkRolesIntegrity(roleRepo);
			
			checkMediaTypeIntegrity(medRepo, env.getProperty("data.mediaTypes").split(","));
			
			checkManagementUser("admin", true, usrRepo, roleRepo, env, passEnc,
				  roleRepo.findOneByName("ROLE_ADMIN"),
				  roleRepo.findOneByName("ROLE_SYSTEM"),
				  roleRepo.findOneByName("ROLE_ANONYMOUS"),
				  roleRepo.findOneByName("ROLE_USER"));
			
			checkManagementUser("system", false, usrRepo, roleRepo, env, passEnc,
				  roleRepo.findOneByName("ROLE_ADMIN"),
				  roleRepo.findOneByName("ROLE_SYSTEM"),
				  roleRepo.findOneByName("ROLE_ANONYMOUS"),
				  roleRepo.findOneByName("ROLE_USER"));
			
			if (usrRepo.findByEmail("user@nat.be") == null) { //TODO Remove for production
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
			
			checkStorageIntegrity(storage, env);
			
		};
	}
	
	private void checkStorageIntegrity(GoogleStorage storage, Environment env) {
		if (!storage.exist(Paths.get(env.getProperty("storage.defaultPic.onlineLocation"))))  //TODO Not ok
			try {
				storage.uploadPicture(
					  Paths.get("/pics/defaultPic.png"),
					  Paths.get(env.getProperty("storage.defaultPic.onlineLocation")),
					  "png"
				);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private void checkManagementUser(String type, boolean admin, UserRepository usrRepo, RoleRepository roleRepo, Environment env, PasswordEncoder passEnc, Role... roles) {
		User usr = usrRepo.findByEmail(env.getProperty("user." + type + ".email")); //TODO Improve
		if (checkAdminIntegrity(usr, env, passEnc, roleRepo))
			if (usr != null)
				usrRepo.delete(usr);
		addManagementUser(type, admin, usrRepo, roleRepo, env, passEnc, roles);
	}
	
	private void addManagementUser(String type, boolean admin, UserRepository usrRepo, RoleRepository roleRepo, Environment env, PasswordEncoder passEnc, Role... roles) {
		if (usrRepo.findByEmail(env.getProperty("user." + type + ".email")) == null)
			usrRepo.save(new User(
				  env.getProperty("user." + type + ".name"),
				  env.getProperty("user." + type + ".email"),
				  passEnc.encode(env.getProperty("user." + type + ".password")),
				  admin,
				  env.getProperty("helmo.storage.defaultPic.onlineLocation"),
				  Arrays.asList(roles)));
		
	}
	
	private boolean checkAdminIntegrity(User usr, Environment env, PasswordEncoder passEnc, RoleRepository roleRepo) {
		return usr != null
			  && usr.getFullName().equals(env.getProperty("user.admin.name"))
			  && usr.getEmail().equals(env.getProperty("user.admin.email"))
			  && passEnc.matches(env.getProperty("user.admin.email"), usr.getPassword())
			  && usr.getOnlinePath().equals(env.getProperty("helmo.storage.defaultPic.onlineLocation"))
			  && usr.isAdmin()
			  && usr.getRoles().contains(roleRepo.findOneByName("ROLE_ADMIN"))
			  && usr.getRoles().contains(roleRepo.findOneByName("ROLE_SYSTEM"))
			  && usr.getRoles().contains(roleRepo.findOneByName("ROLE_ANONYMOUS"))
			  && usr.getRoles().contains(roleRepo.findOneByName("ROLE_USER"));
	}
	
	private boolean checkSystemIntegrity(User usr, Environment env, PasswordEncoder passEnc, RoleRepository roleRepo) {
		return usr != null
			  && usr.getFullName().equals(env.getProperty("user.system.name"))
			  && usr.getEmail().equals(env.getProperty("user.system.email"))
			  && passEnc.matches(env.getProperty("user.system.password"), usr.getPassword())
			  && usr.getOnlinePath().equals(env.getProperty("helmo.storage.defaultPic.onlineLocation"))
			  && !usr.isAdmin()
			  && usr.getRoles().contains(roleRepo.findOneByName("ROLE_SYSTEM"))
			  && usr.getRoles().contains(roleRepo.findOneByName("ROLE_ANONYMOUS"))
			  && usr.getRoles().contains(roleRepo.findOneByName("ROLE_USER"));
		
	}
	
	private void checkMediaTypeIntegrity(MediaTypeRepository medRepo, String ... mediaTypeNames) {
		List<MediaType> mediaTypes = new ArrayList<>();
		for(String str : mediaTypeNames)
			if(medRepo.findByName(str) == null)
				mediaTypes.add(createMediaType(str));
				
		medRepo.save(mediaTypes);
	}
	private MediaType createMediaType(String name) {
		MediaType rtn = new MediaType();
		rtn.setName(name);
		return rtn;
	}
	
	private void checkRolesIntegrity(RoleRepository roleRepo) { //TODO Apply same principle as MediaTypes
		List<Role> roles = new ArrayList<>();
		if (roleRepo.findOneByName("ROLE_ADMIN") == null) {
			Role adm = new Role();
			adm.setName("ROLE_ADMIN");
			adm.setDescription("Natagora's Super Administrator");
			roles.add(adm);
		}
		if (roleRepo.findOneByName("ROLE_SYSTEM") == null) {
			Role sys = new Role();
			sys.setName("ROLE_SYSTEM");
			sys.setDescription("Allow a non human user to do some actions");
			roles.add(sys);
		}
		if (roleRepo.findOneByName("ROLE_USER") == null) {
			Role usr = new Role();
			usr.setName("ROLE_USER");
			usr.setDescription("Simple user");
			roles.add(usr);
		}
		if (roleRepo.findOneByName("ROLE_ANONYMOUS") == null) {
			Role usr = new Role();
			usr.setName("ROLE_ANONYMOUS");
			usr.setDescription("Unauthenticated user");
			roles.add(usr);
		}
		roleRepo.save(roles);
	}
}
