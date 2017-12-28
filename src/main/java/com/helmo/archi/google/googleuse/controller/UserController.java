package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/*
 * WARNING: The code that follows may make you cry:
 *           A Safety Pig has been provided below for your benefit
 *                              _
 *      _._ _..._ .-',     _.._(`))
 *     '-. `     '  /-._.-'    ',/
 *       )         \            '.
 *      / _    _    |             \
 *     |  a    a    /              |
 *      \   .-.                     ;
 *       '-('' ).-'       ,'       ;
 *          '-;           |      .'
 *            \           \    /
 *            | 7  .__  _.-\   \
 *            | |  |  ``/  /`  /
 *           /,_|  |   /,_/   /
 *              /,_/      '`-'
 */

@RestController
@RequestMapping("/users")
public class UserController implements BasicController<User> {
	
	
	private final UserService usrSrv;
	
	private final List<User> SUPER_USERS; //SuperUsers in cache
	
	public UserController(UserService usrSrv, Environment env) {
		this.usrSrv = usrSrv;
		SUPER_USERS = Arrays.asList(     //Define the cache
			  usrSrv.getByEmail(env.getProperty("user.admin.email")),
			  usrSrv.getByEmail(env.getProperty("user.system.email")),
			  usrSrv.getByEmail(env.getProperty("user.default.email")),
			  usrSrv.getByEmail(env.getProperty("user.anonymous.email")));
		
	}
	
	@Override
	@GetMapping
	@Secured("ROLE_SYSTEM")
	public List<User> getAll() {
		return usrSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	@Secured("ROLE_SYSTEM")
	public User getOne(@PathVariable("id") long id) {
		return usrSrv.getById(id);
	}
	
	@GetMapping("/email")
	public User getByEmail() {
		return usrSrv.getByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
	}
	
	@GetMapping("/{one}/{two}")
	@Secured("ROLE_USER")
	public List<User> getRange(@PathVariable("one") long one, @PathVariable("two") long two) {
		if (two <= one) throw new IllegalArgumentException("Wrong args");
		return usrSrv.getRange(one, two);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_SYSTEM")
	public ResponseEntity create(@RequestBody User... users) {
		try {
			List<User> rtn = usrSrv.create(users);
			return ResponseEntity.ok(rtn);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public ResponseEntity update(@RequestBody User... users) {
		try {
			List<User> rtn = usrSrv.update(users);
			return ResponseEntity.ok(rtn);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
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
			if (checkAdmin(usr)) //SuperAdmin and System can't be changed //TODO Report this check to the service
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
		return usr != null && SUPER_USERS.contains(usr);
	} //Comparison based on ID
}
