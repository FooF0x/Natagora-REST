package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserRepository usrRepo;
	private final PasswordEncoder passEnc;
	private final Environment env;
	
	public UserService(UserRepository usrRepo, PasswordEncoder passEnc, Environment env) {
		this.usrRepo = usrRepo;
		this.passEnc = passEnc;
		this.env = env;
	}
	
	public List<User> getUsers() {
		return usrRepo.findAll();
	}
	
	public void createUser(User usr) {
		User two = new User();
		two.setFullName(usr.getFullName());
		two.setEmail(usr.getEmail());
		two.setAdmin(usr.isAdmin());
		two.setPassword(passEnc.encode(usr.getPassword()));
		two.setOnlinePath(usr.getOnlinePath() != null ? usr.getOnlinePath() : env.getProperty("helmo.storage.defaultPic.onlineLocation"));
		two.setRoles(usr.getRoles());
		usrRepo.save(two);
	}
	
	public User getById(long id) {
		return usrRepo.findOne(id);
	}
	
	public void deleteById(long id) {
		usrRepo.delete(id);
	}
}
