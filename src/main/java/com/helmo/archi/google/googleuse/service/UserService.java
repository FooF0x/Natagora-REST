package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserRepository usrRepo;
	
	public UserService(UserRepository usrRepo) {
		this.usrRepo = usrRepo;
	}
	
	public List<User> getUsers() {
		return usrRepo.findAll();
	}
	
	public void createUser(User usr) {
		User two = new User();
		two.setFullName(usr.getFullName());
		two.setEmail(usr.getEmail());
		two.setAdmin(usr.isAdmin());
		two.setOnlinePath(usr.getOnlinePath());
		usrRepo.save(two);
	}
}
