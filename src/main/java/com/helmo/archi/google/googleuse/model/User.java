package com.helmo.archi.google.googleuse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Entity
@Table(name = "users")
@Getter
@Setter
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends IdentifiedModel {
	
	@Column(name = "full_name")
	@NotEmpty
	private String fullName;
	
	@Column(name = "email")
//	@Email(message = "Please, provide a valid email")
	@NotEmpty
	private String email;
	
	@Column(name = "is_admin")
	private boolean admin = false;
	
	@Column(name = "pic_path")
	private String onlinePath;
	
	@Column(name = "public_link")
	private String publicLink;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinTable(
		  name = "user_role",
		  joinColumns = @JoinColumn(name = "id_user"),
		  inverseJoinColumns = @JoinColumn(name = "id_role"))
	private List<Role> roles;
	
	@OneToMany(cascade = {CascadeType.ALL},
		  mappedBy = "user")
	@JsonProperty(access = WRITE_ONLY)
	private List<Session> sessions;
	
	@Column(name = "password")
	@JsonProperty(access = WRITE_ONLY)
	private String password;
	
	public User() {
	}
	
	public User(String fullName, String email, String pass, boolean admin, String onlinePath, List<Role> roles) {
		this.fullName = fullName;
		this.email = email;
		this.admin = admin;
		this.password = pass;
		this.onlinePath = onlinePath;
		this.roles = roles;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User)) return false;
		User that = (User) o;
		
		return getId() == that.getId() && email.equals(that.email);
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(getId()) + email.hashCode();
	}
}
