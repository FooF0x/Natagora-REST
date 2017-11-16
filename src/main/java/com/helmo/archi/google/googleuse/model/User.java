package com.helmo.archi.google.googleuse.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User extends IdentifiedModel {
	
	@Column(name = "full_name")
	private String fullName;
	@Column(name = "email")
	private String email;
	@Column(name = "is_admin")
	private boolean admin = false;
	
	@Column(name = "pic_path")
	private String onlinePath;
	
//	@JsonManagedReference
	@OneToMany(cascade = {CascadeType.PERSIST},
			mappedBy = "father")
	private List<Session> sessions;
	
	public User() {}
	
	public User(String fullName, String email) {
		this.fullName = fullName;
		this.email = email;
		sessions = new ArrayList<>();
	}
}
