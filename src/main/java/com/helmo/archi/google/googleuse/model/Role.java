package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends IdentifiedModel {
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	public Role() {
	}
	
	public Role(String name, String desc) {
		this.name = name;
		this.description = desc;
	}
}
