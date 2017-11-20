package com.helmo.archi.google.googleuse.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role extends IdentifiedModel{
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	public Role() {}
}
