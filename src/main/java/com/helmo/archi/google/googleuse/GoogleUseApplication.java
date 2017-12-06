package com.helmo.archi.google.googleuse;

import com.helmo.archi.google.googleuse.model.MediaType;
import com.helmo.archi.google.googleuse.model.NotificationStatus;
import com.helmo.archi.google.googleuse.model.Role;
import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.MediaTypeRepository;
import com.helmo.archi.google.googleuse.repository.NotificationStatusRepository;
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
	                                  PasswordEncoder passEnc, Environment env, GoogleStorage storage,
	                                  NotificationStatusRepository notRepo) {
		return args -> {
			
			checkRolesIntegrity(roleRepo, env, env.getProperty("data.role.property-names").split(","));
			
			checkMediaTypeIntegrity(medRepo, env.getProperty("data.mediaTypes").split(","));
			
			checkNotificationStatusIntegrity(notRepo, env.getProperty("data.not-status").split(","));
			
			checkManagementUser(
				  "admin",
				  usrRepo, env, passEnc,
				  createUser(
				  	  "admin",
					    roleRepo,
					    env,
					    passEnc));
			
			checkManagementUser(
				  "system",
				  usrRepo, env, passEnc,
				  createUser(
						"system",
						roleRepo,
						env,
						passEnc));
			
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
	
	private User createUser(String type, RoleRepository roleRepo, Environment env, PasswordEncoder passEnc) {
		User rtn = new User();
		rtn.setFullName(env.getProperty("user." + type + ".name"));
		rtn.setEmail(env.getProperty("user." + type + ".email"));
		rtn.setAdmin(Boolean.parseBoolean(env.getProperty("user." + type + ".is-admin")));
		rtn.setOnlinePath(env.getProperty("helmo.storage.defaultPic.onlineLocation"));
		rtn.setPassword(passEnc.encode(env.getProperty("user." + type + ".password")));
		rtn.setSessions(new ArrayList<>());
		List<Role> roles = new ArrayList<>();
		for(String str : env.getProperty("user." + type + ".roles").split(","))
			roles.add(roleRepo.findOneByName(str));
		rtn.setRoles(roles);
		return rtn;
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
	
	private void checkManagementUser(String type, UserRepository usrRepo, Environment env,
	                                  PasswordEncoder passEnc, User haveToBe) {
		User dbUser = usrRepo.findByEmail(env.getProperty("user." + type + ".email"));
		if(dbUser == null || compareUsers(dbUser, haveToBe, type, passEnc, env)) {
			usrRepo.delete(dbUser);
			usrRepo.save(haveToBe);
		}
	}
	
	private boolean compareUsers(User dbUser, User haveToBe, String type, PasswordEncoder passEnc, Environment env) {
		boolean rtn = dbUser.getFullName().equals(haveToBe.getFullName())
			  && dbUser.getEmail().equals(haveToBe.getEmail())
			  && dbUser.getOnlinePath().equals(haveToBe.getOnlinePath())
			  && dbUser.isAdmin() == Boolean.parseBoolean(env.getProperty("user." + type + "is-admin"))
			  && dbUser.getSessions().size() == 0
			  && passEnc.matches(
			  	  env.getProperty("user." + type + ".password"), dbUser.getPassword()
				);
		
		for(Role role : haveToBe.getRoles()) {
			if (!dbUser.getRoles().contains(role)) {
				rtn = false;
				break;
			}
		}
		
		return rtn;
	}
	
	private void checkNotificationStatusIntegrity(NotificationStatusRepository notRepo, String... status) {
		List<NotificationStatus> rtn = new ArrayList<>();
		for(String str : status)
			if(notRepo.findByName(str) == null)
				rtn.add(createStatus(str));
		notRepo.save(rtn);
	}
	
	private NotificationStatus createStatus(String name) {
		NotificationStatus rtn = new NotificationStatus();
		rtn.setName(name);
		return rtn;
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
	
	private void checkRolesIntegrity(RoleRepository roleRepo, Environment env, String... roleNames) {
		List<Role> roles = new ArrayList<>();
		for(String str : roleNames)
			if(roleRepo.findOneByName(env.getProperty("data.role." + str + ".name")) == null)
				roles.add(createRole(
					  env.getProperty("data.role." + str + ".name"),
					  env.getProperty("data.role." + str + ".description")
				));
		roleRepo.save(roles);
	}
	
	private Role createRole(String name, String description) {
		Role role = new Role();
		role.setName(name);
		role.setDescription(description);
		return role;
	}
}
