package com.helmo.archi.google.googleuse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Entity
@Table(name = "sessions")
@Getter
@Setter
public class Session extends IdentifiedModel {
	
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
	
	@OneToMany(cascade = {CascadeType.PERSIST},
		  mappedBy = "session")
	private List<Observation> observations;
	
	@JoinColumn(name = "id_user")
	@ManyToOne(targetEntity = User.class)
	@JsonProperty(access = WRITE_ONLY)
	private User user;
	
	public Session() {
	}
	
}
