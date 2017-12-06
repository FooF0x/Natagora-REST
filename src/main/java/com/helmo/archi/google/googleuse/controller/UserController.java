package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController implements BasicController<User> {
	
	private final UserService usrSrv;
	
	private final List<User> superUsers; //SuperUsers in cache
	
	public UserController(UserService usrSrv, Environment env) {
		this.usrSrv = usrSrv;
		superUsers = Arrays.asList(     //Define the cache
			  usrSrv.getByEmail(env.getProperty("user.admin.email")),
			  usrSrv.getByEmail(env.getProperty("user.system.email")));
	}
	
	@Override
	@GetMapping
	@Secured("ROLE_ANONYMOUS")
	public List<User> getAll() {
		return usrSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	@Secured("ROLE_ANONYMOUS")
	public User getOne(@PathVariable("id") long id) {
		return usrSrv.getById(id);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_SYSTEM")
	public ResponseEntity create(@RequestBody User... users) {
		try {
			List<User> rtn = new ArrayList<>();
			for (User usr : users)
				if (!checkAdmin(usr)) //SuperAdmin and System can't be changed
					rtn.add(usrSrv.create(usr));
			return ResponseEntity.ok(rtn);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public ResponseEntity update(@RequestBody User... users) {
		try {
			List<User> rtn = new ArrayList<>();
			for (User usr : users)
				if (!checkAdmin(usr)) //SuperAdmin and System can't be changed
					rtn.add(usrSrv.update(usr));
			return ResponseEntity.ok(rtn);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
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
		for (User usr : users) {
			if (checkAdmin(usr)) //SuperAdmin and System can't be changed
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
			
			usrSrv.deleteById(usr.getId());
		}
		return ResponseEntity.ok(null);
	}
	
	/**
	 * Check weather a user is simple or admin.
	 *
	 * @param usr The user you wanna check
	 * @return <code>TRUE</code> if is admin or system user
	 */
	private boolean checkAdmin(User usr) {
		return usr != null && superUsers.contains(usr);
	}
}
