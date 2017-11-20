package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "passwords")
@Getter @Setter
public class Password extends IdentifiedModel {

	@Column(name = "content")
	private String content;
	
	@Column(name = "date_change")
	private Timestamp dataChange;
	
	@JoinColumn(name = "id_user")
	@ManyToOne(targetEntity = User.class)
	private User user;
	
	public Password() { }
}
