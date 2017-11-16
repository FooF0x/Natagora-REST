package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.nio.file.Path;
import java.sql.Timestamp;

//@Entity(name = "Observation")
@Table(name = "Observation")
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
	@Column(name = "media_type")
	private int mediaType;
	@Column(name = "latitude")
	private int bird;
	private Path onlinePath;
	@Column(name = "latitude")
	private String analyseResult;
	
	@Column(name = "latitude")
	private Path localPath;
	
	private Session father;
	
	public Observation() {}
	
	public Observation(
			Session session, String latitude, String longitude, Timestamp dateTime, int nbrObs, boolean validation,
			int mediaType, int bird, Path localPath, Path onlinePath) {
		this.father = session;
		this.latitude = latitude;
		this.longitude = longitude;
		this.dateTime = dateTime;
		this.nbrObs = nbrObs;
		this.validation = validation;
		this.mediaType = mediaType;
		this.bird = bird;
		this.localPath = localPath;
		this.onlinePath = onlinePath;
	}
	
	public String getLocalAsString(){
		return localPath.toString();
	}
	
	public String getOnlineAsString() {
		return onlinePath.toString();
	}
	
	}
