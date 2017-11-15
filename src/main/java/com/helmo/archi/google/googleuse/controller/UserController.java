package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final UserService usrSrv;
	
	public UserController(UserService usrSrv) {
		this.usrSrv = usrSrv;
	}
	
	@GetMapping()
	public List<User> getUsers() {
		return usrSrv.getUsers();
	}
	
	@PostMapping()
	public void createUser(@RequestBody User usr) {
		usrSrv.createUser(usr);
	}
}
