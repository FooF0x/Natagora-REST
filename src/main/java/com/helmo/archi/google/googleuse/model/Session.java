package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

//@Entity(name = "Session")
@Table(name = "Session")
@Getter @Setter
public class Session {
	
	@Id
	private long idSession;
	
	private String name;
	
	private Timestamp dateStart;
	private Timestamp dateEnd;
	
	private String latitude;
	private String longitude;
	
//	@OneToMany(cascade = {CascadeType.PERSIST})
//	private List<Observation> observations;
	
	
	private User father;
	
	public Session() {}
	
	public Session(User user, String name, Timestamp start, Timestamp end, String latitude, String longitude) {
		this.father = user;
		this.name = name;
		this.dateStart = start;
		this.dateEnd = end;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
