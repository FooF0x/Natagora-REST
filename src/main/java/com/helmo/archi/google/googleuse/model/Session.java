package com.helmo.archi.google.googleuse.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "session")
@Getter @Setter
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Session extends IdentifiedModel{
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "date_start")
	private Timestamp dateStart;
	@Column(name = "date_end")
	private Timestamp dateEnd;
	
	
	@Column(name = "latitude")
	private String latitude;
	@Column(name = "longitude")
	private String longitude;
	
//	@OneToMany(cascade = {CascadeType.PERSIST})
//	private List<Observation> observations;

//	@JsonBackReference
	@JoinColumn(name = "id_user")
	@ManyToOne(targetEntity = User.class)
	private User father;
	
	public Session() {}
	
//	public Session(User user, String name, Timestamp start, Timestamp end, String latitude, String longitude) {
////		this.father = user;
//		this.name = name;
//		this.dateStart = start;
//		this.dateEnd = end;
//		this.latitude = latitude;
//		this.longitude = longitude;
//	}
}
