package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserRepository usrRepo;
	private final PasswordEncoder passEnc;
	
	public UserService(UserRepository usrRepo, PasswordEncoder passEnc) {
		this.usrRepo = usrRepo;
		this.passEnc = passEnc;
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
		two.setOnlinePath(usr.getOnlinePath() != null ? usr.getOnlinePath() : "default/defProfilePic.png");
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
