package com.helmo.archi.google.googleuse;

import com.helmo.archi.google.googleuse.model.*;
import com.helmo.archi.google.googleuse.repository.*;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@SpringBootApplication
public class GoogleUseApplication extends SpringBootServletInitializer {
	
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private UserRepository usrRepo;
	@Autowired
	private MediaTypeRepository medRepo;
	@Autowired
	private BirdRepository brdRepo;
	@Autowired
	private PasswordEncoder passEnc;
	@Autowired
	private Environment env;
	@Autowired
	private GoogleStorage storage;
	@Autowired
	private NotificationStatusRepository notRepo;
	
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
	public ApplicationRunner startApp() {
		return args -> {
			
			checkBirdIntegrity();
			
			checkRolesIntegrity(env.getProperty("data.role.property-names").split(","));
			
			checkMediaTypeIntegrity(env.getProperty("data.mediaTypes").split(","));
			
			checkNotificationStatusIntegrity(env.getProperty("data.not-status").split(","));
			
			checkManagementUser(
				  "admin",
				  createUser("admin"));
			
			checkManagementUser(
				  "system",
				  createUser("system"));
			
			checkManagementUser(
				  "default",
				  createUser("default"));

//			checkStorageIntegrity();
			
		};
	}
	
	private void checkBirdIntegrity() {
		if (!brdRepo.exists(0L)) { //TODO if exist, check values integrity
			Bird bird = new Bird();
			bird.setName("Unknown bird");
			bird.setDescription("This is an unknown bird");
			brdRepo.insert(bird);
		}
	}
	
	private User createUser(String type) {
		User rtn = new User();
		rtn.setFullName(env.getProperty("user." + type + ".name"));
		rtn.setEmail(env.getProperty("user." + type + ".email"));
		rtn.setAdmin(Boolean.parseBoolean(env.getProperty("user." + type + ".is-admin")));
		rtn.setOnlinePath(env.getProperty("storage.defaultPic.onlineLocation"));
		rtn.setPassword(passEnc.encode(env.getProperty("user." + type + ".password")));
		rtn.setSessions(new ArrayList<>());
		List<Role> roles = new ArrayList<>();
		for (String str : env.getProperty("user." + type + ".roles").split(","))
			roles.add(roleRepo.findOneByName(str));
		rtn.setRoles(roles);
		return rtn;
	}
	
	private void checkStorageIntegrity() {
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
	
	private void checkManagementUser(String type, User haveToBe) {
		User dbUser = usrRepo.findByEmail(env.getProperty("user." + type + ".email"));
		if (dbUser == null) {
			usrRepo.save(haveToBe);
		} else if (!compareUsers(dbUser, haveToBe, type)) {
			usrRepo.delete(dbUser);
			usrRepo.save(haveToBe);
		}
	}
	
	private boolean compareUsers(User dbUser, User haveToBe, String type) {
		try {
			boolean rtn = dbUser.getFullName().equals(haveToBe.getFullName())
				  && dbUser.getEmail().equals(haveToBe.getEmail())
				  && dbUser.getOnlinePath().equals(haveToBe.getOnlinePath())
				  && dbUser.isAdmin() == haveToBe.isAdmin()
				  && passEnc.matches(
				  env.getProperty("user." + type + ".password"), dbUser.getPassword()
			);
			for (Role role : haveToBe.getRoles()) {
				if (!dbUser.getRoles().contains(role)) {
					rtn = false;
					break;
				}
			}
			return rtn;
		} catch (NullPointerException ex) {
			return false;
		}
	}
	
	private void checkNotificationStatusIntegrity(String... status) {
		List<NotificationStatus> rtn = new ArrayList<>();
		for (String str : status)
			if (notRepo.findByName(str) == null)
				rtn.add(createStatus(str));
		notRepo.save(rtn);
	}
	
	private NotificationStatus createStatus(String name) {
		NotificationStatus rtn = new NotificationStatus();
		rtn.setName(name);
		return rtn;
	}
	
	private void checkMediaTypeIntegrity(String... mediaTypeNames) {
		List<MediaType> mediaTypes = new ArrayList<>();
		for (String str : mediaTypeNames)
			if (medRepo.findByName(str) == null)
				mediaTypes.add(createMediaType(str));
		
		medRepo.save(mediaTypes);
	}
	
	private MediaType createMediaType(String name) {
		MediaType rtn = new MediaType();
		rtn.setName(name);
		return rtn;
	}
	
	private void checkRolesIntegrity(String... roleNames) {
		List<Role> roles = new ArrayList<>();
		for (String str : roleNames)
			if (roleRepo.findOneByName(env.getProperty("data.role." + str + ".name")) == null)
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
