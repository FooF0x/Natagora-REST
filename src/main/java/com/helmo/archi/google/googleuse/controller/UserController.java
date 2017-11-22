package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.service.PasswordService;
import com.helmo.archi.google.googleuse.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final UserService usrSrv;
	private final PasswordService pwdSrv;
	
	public UserController(UserService usrSrv, PasswordService pwdSrv) {
		this.usrSrv = usrSrv;
		this.pwdSrv = pwdSrv;
	}
	
	@GetMapping()
	@Secured("ROLE_ADMIN")
	public List<User> getUsers() {
		return usrSrv.getUsers();
	}
	
	@PostMapping()
	public void createUser(@RequestBody User usr) {
		usrSrv.createUser(usr);
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable("id") long id) {
		return usrSrv.getById(id);
	}

	@PutMapping() //TODO JWTToken
	public void updateUserById(@RequestBody User usr) {
		usrSrv.createUser(usr);
	}

	@DeleteMapping("/{id}")
	public void deleteUserById(@PathVariable("id") long id) {
		usrSrv.deleteById(id);
	}
}
