package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.service.PasswordService;
import com.helmo.archi.google.googleuse.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController implements BasicController<User> {
	
	private final UserService usrSrv;
	private final PasswordService pwdSrv;
	
	public UserController(UserService usrSrv, PasswordService pwdSrv) {
		this.usrSrv = usrSrv;
		this.pwdSrv = pwdSrv;
	}
	
	@Override
	@GetMapping
	@Secured("ROLE_SYSTEM")
	public List<User> getAll() {
		return usrSrv.getUsers();
	}
	
	@Override
	@GetMapping("/{id}")
	@Secured("ROLE_SYSTEM")
	public User getOne(@PathVariable("id") long id) {
		return usrSrv.getById(id);
	}
	
	@Override
	@PostMapping
	public List<User> create(@RequestBody User... users) {
		return null;
	}
	
//	@Override
//	@PutMapping
//	@Secured("ROLE_USER")
//	public User updateOne(@RequestBody User usr) {
//		if (checkAdmin(usr)) //SuperAdmin and System can't be changed
//			//return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//			return null;
//
//		return usrSrv.update(usr);
////		return ResponseEntity.ok(null);
//	}
	
	@Override
	@PutMapping
	public List<User> update(@RequestBody User... users) {
		List<User> rtn = new ArrayList<>();
		for(User usr : users)
			rtn.add(usrSrv.update(usr));
		return rtn;
	}
	
	@Override
	@DeleteMapping("/{id}")
	@Secured("ROLE_USER")
	public ResponseEntity deleteOne(@PathVariable("id") long id) {
		if (checkAdmin(usrSrv.getById(id))) //SuperAdmin and System can't be changed
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

		usrSrv.deleteById(id);
		return ResponseEntity.ok(null);
	}
	
	@Override
	@DeleteMapping
	public ResponseEntity delete(@RequestBody User... users) {
		return ResponseEntity.badRequest().build();
	}
	
	private boolean checkAdmin(@RequestBody User usr) {
		return usr != null &&
			  (usr.getEmail().equals("admin@nat.be") || usr.getEmail().equals("system@nat.be"));
	}
}
