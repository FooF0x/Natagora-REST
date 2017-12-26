package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.RoleRepository;
import com.helmo.archi.google.googleuse.repository.SessionRepository;
import com.helmo.archi.google.googleuse.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements AccessRange<User, Long> {
	
	private final UserRepository usrRepo;
	private final RoleRepository roleRepo;
	private final SessionRepository sesRepo;
	
	private final PasswordEncoder passEnc;
	private final Environment env;
	
	private final List<User> SUPER_USERS;
	private final User DEFAULT_USER;
	
	public UserService(UserRepository usrRepo, RoleRepository roleRepo, SessionRepository sesRepo,
	                   PasswordEncoder passEnc, Environment env) {
		this.usrRepo = usrRepo;
		this.roleRepo = roleRepo;
		this.sesRepo = sesRepo;
		this.passEnc = passEnc;
		this.env = env;
		
		DEFAULT_USER = getByEmail(env.getProperty("user.default.email"));
		SUPER_USERS = Arrays.asList(     //Define the cache
			  getByEmail(env.getProperty("user.admin.email")),
			  getByEmail(env.getProperty("user.system.email")),
			  DEFAULT_USER);
	}
	
	@Override
	public List<User> getAll() {
		return usrRepo.findAll();
	}
	
	@Override
	public User getById(Long id) {
		return usrRepo.findOne(id);
	}
	
	public User getByEmail(String email) {
		return usrRepo.findByEmail(email);
	}
	
	@Override
	public User create(User usr) {
		if (usr.getRoles() == null || usr.getRoles().size() == 0)
			usr.setRoles(Collections.singletonList(roleRepo.findOneByName("ROLE_USER")));
		
		User two = new User();
		two.setFullName(usr.getFullName());
		two.setEmail(usr.getEmail());
		two.setAdmin(usr.isAdmin());
		two.setPassword(passEnc.encode(usr.getPassword()));
		two.setOnlinePath(
			  (usr.getOnlinePath() != null)
					? usr.getOnlinePath()
					: env.getProperty("storage.defaultPic.onlineLocation"));
		two.setRoles(usr.getRoles());
		return usrRepo.save(two);
	}
	
	public User update(User toUpdate) {
		if (checkAdmin(toUpdate))
			return null;
		
		User usr = usrRepo.findOne(toUpdate.getId());
		usr.setFullName(
			  toUpdate.getFullName() != null
					? toUpdate.getFullName()
					: usr.getFullName()
		);
		usr.setEmail(
			  toUpdate.getEmail() != null
					? toUpdate.getEmail()
					: usr.getEmail()
		);
		usr.setOnlinePath(
			  toUpdate.getOnlinePath() != null
					? toUpdate.getOnlinePath()
					: usr.getOnlinePath()
		);
		usr.setRoles(
			  toUpdate.getRoles() != null
					? toUpdate.getRoles()
					: usr.getRoles()
		);
		usr.setAdmin(toUpdate.isAdmin());
		return usrRepo.save(usr);
		
	}
	
	@Override
	public void delete(User... users) {
		Arrays.asList(users).forEach(this::delete);
	}
	
	@Override
	public void delete(User user) {
		if (!checkAdmin(user)) {
			List<Session> sessions = sesRepo.findByUser(user);
			sessions.forEach(s -> s.setUser(DEFAULT_USER));
			sesRepo.save(sessions);
			usrRepo.delete(user);
		}
	}
	
	@Override
	public void deleteById(Long id) {
		List<Session> sessions = sesRepo.findByUser_Id(id);
		sessions.forEach(s -> s.setUser(DEFAULT_USER));
		sesRepo.save(sessions);
		usrRepo.delete(id);
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
