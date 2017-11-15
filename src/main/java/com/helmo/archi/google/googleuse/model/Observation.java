package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.nio.file.Path;
import java.sql.Timestamp;

//@Entity(name = "Observation")
@Table(name = "Observation")
@Getter @Setter
public class Observation { //TODO Define as a SQLObsElement
	
	@Id
	private long idObs;
	private String latitude;
	private String longitude;
	private Timestamp dateTime;
	
	private int nbrObs;
	private boolean validation;
	private int mediaType;
	private int bird;
	private Path onlinePath;
	private String analyseResult;
	
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
	
	public Path getLocalPath() {
		return localPath;
	}
	
	public void setLocalPath(Path localPath) {
		this.localPath = localPath;
	}
	
	public long getIdObs() {
		return idObs;
	}
	
	public void setIdObs(long idObs) {
		this.idObs = idObs;
	}
	
	public long getIdSes() {
		return father.getIdSession();
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public Timestamp getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}
	
	public int getNbrObs() {
		return nbrObs;
	}
	
	public void setNbrObs(int nbrObs) {
		this.nbrObs = nbrObs;
	}
	
	public boolean isValidation() {
		return validation;
	}
	
	public void setValidation(boolean validation) {
		this.validation = validation;
	}
	
	public int getMediaType() {
		return mediaType;
	}
	
	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}
	
	public int getBird() {
		return bird;
	}
	
	public void setBird(int bird) {
		this.bird = bird;
	}
	
	public Path getOnlinePath() {
		return onlinePath;
	}
	
	public void setOnlinePath(Path onlinePath) {
		this.onlinePath = onlinePath;
	}
	
	public String getAnalyseResult() {
		return analyseResult;
	}
	
	public void setAnalyseResult(String analyseResult) {
		this.analyseResult = analyseResult;
	}
}
