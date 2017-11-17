package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DatastoreIdentifiedModel {
	@Id
	@Getter @Setter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DatastoreIdentifiedModel)) return false;
		DatastoreIdentifiedModel that = (DatastoreIdentifiedModel) o;
		
		return id == that.id;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}
}
