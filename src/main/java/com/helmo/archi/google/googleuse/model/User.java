package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.nio.file.Path;

@Entity
@Table(name = "`Users`")
@Getter @Setter
public class User extends IdentifiedModel {
	
	@Column(name = "full_name")
	private String fullName;
	@Column(name = "email")
	private String email;
	@Column(name = "is_admin")
	private boolean admin = false;
	
	@Column(name = "pic_path")
	private String onlinePath;
	
//	@OneToMany()
//	protected List<Session> sessions;
	
	public User() {}
	
	public User(String fullName, String email) {
		this.fullName = fullName;
		this.email = email;
//		sessions = new ArrayList<>();
	}
	
	protected User(String fullName, String email, Path onlinePath) {
		this(fullName, email);
		this.onlinePath = onlinePath.toString();
	}
}
