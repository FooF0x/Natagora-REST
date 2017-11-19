package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "report")
@Getter @Setter
public class Report extends IdentifiedModel {

	@Column(name = "user_comment")
	private String commentary;
	
	@Column(name = "date_time")
	private Timestamp date;
	
	@JoinColumn(name = "id_user")
	@ManyToOne(targetEntity = User.class)
	private User user;
	
	@JoinColumn(name = "id_observation")
	@ManyToOne(targetEntity = Observation.class)
	private Observation observation;
	
	public Report() {}
}
