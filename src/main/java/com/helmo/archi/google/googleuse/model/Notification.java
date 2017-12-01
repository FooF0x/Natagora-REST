package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification extends IdentifiedModel {
	
	@Column(name = "caption")
	private String caption;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "date_time")
	private Timestamp date;
	
	@Column(name = "status")
	private boolean status;
	
	@JoinColumn(name = "observation")
	@ManyToOne(
		  targetEntity = Observation.class,
		  cascade = CascadeType.REMOVE)
	private Observation observation;
	
	public Notification() {
	}
}
