package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.service.PasswordService;
import com.helmo.archi.google.googleuse.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	@Secured({"ROLE_ADMIN", "ROLE_SYSTEM"})
	public List<User> getUsers() {
		return usrSrv.getUsers();
	}
	
	@PostMapping()
	@Secured({"ROLE_ADMIN", "ROLE_SYSTEM"})
	public void createUser(@RequestBody User usr) {
		usrSrv.createUser(usr);
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable("id") long id) {
		return usrSrv.getById(id);
	}

	@PutMapping()
	public ResponseEntity updateUserById(@RequestBody User usr) {
		if(checkAdmin(usr)) //SuperAdmin and System can't be changed
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		
		usrSrv.createUser(usr);
		return ResponseEntity.ok(null);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteUserById(@PathVariable("id") long id) {
		if(checkAdmin(usrSrv.getById(id))) //SuperAdmin and System can't be changed
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		
		usrSrv.deleteById(id);
		return ResponseEntity.ok(null);
	}
	
	private boolean checkAdmin(User usr) {
		return usr != null &&
				(usr.getEmail().equals("admin@nat.be") || usr.getEmail().equals("system@nat.be"));
	}
}
