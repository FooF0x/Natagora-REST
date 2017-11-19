package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class Bird extends DatastoreIdentifiedModel {
	
	public static final String NAME = "name"; //Used with Datastore
	public static final String DESCRIPTION = "description";
	public static final String DATA = "data";
	public static final String PICTURE = "picture";
	
	private String name;
	private String description;
	private Map<String, String> data;
	private List<String> picture;
	
	public Bird() {}
	
	public String get(String key) {
		return data.get(key);
	}
	
	public void put(String key, String value) {
		data.put(key, value);
	}
}
