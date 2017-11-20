package com.helmo.archi.google.googleuse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.nio.file.Path;
import java.sql.Timestamp;

@Entity
@Table(name = "observations")
@Getter @Setter
public class Observation extends IdentifiedModel {
	
	@Column(name = "latitude")
	private String latitude;
	
	@Column(name = "longitude")
	private String longitude;
	
	@Column(name = "date_time")
	private Timestamp dateTime;
	
	@Column(name = "number_obs")
	private int nbrObs;
	
	@Column(name = "validation")
	private boolean validation;
	
	@Column(name = "online_path")
	private String onlinePath;
	
	@Column(name = "analyse_result")
	private String analyseResult;
	
	@Column(name = "id_bird")
	private long birdId;
	
	@JoinColumn(name = "media_type")
	@ManyToOne(targetEntity = MediaType.class)
	private MediaType mediaType;
	
	@JoinColumn(name = "id_session")
	@ManyToOne(targetEntity = Session.class)
	@JsonIgnore
	private Session session;
	
	@Transient
	@JsonIgnore
	private Path localPath;
	
	@Transient
	private Bird bird;
	
	public Observation() {}
}
