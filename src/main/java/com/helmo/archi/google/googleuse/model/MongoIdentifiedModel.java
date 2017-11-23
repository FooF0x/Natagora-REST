package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public abstract class MongoIdentifiedModel {
	
	@Id
	@Getter @Setter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MongoIdentifiedModel)) return false;
		MongoIdentifiedModel that = (MongoIdentifiedModel) o;
		
		return id == that.id;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}
}
