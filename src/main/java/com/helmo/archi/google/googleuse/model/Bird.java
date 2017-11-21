package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class Bird extends DatastoreIdentifiedModel {
	
	public static final String NAME = "name"; //Used with Datastore
	public static final String DESCRIPTION = "description";
	public static final String DATA = "data";
	public static final String PICTURE = "picture";
	public static final String MULTIPLE = "multiple";
	
	private String name;
	private String description;
	private List<String> picture;
	
	private Map<String, String> data;
	private Map<String, List<String>> multiple;
	
	public Bird() {}
	
	public String getFromData(String key) {
		return data.get(key);
	}
	
	public List<String> getFromMultiple(String key) {
		return multiple.get(key);
	}
	
	public String getFromPicture(int idx) {
		return picture.get(idx);
	}
	
	public void putIntoData(String key, String value) {
		data.put(key, value);
	}
	
	public void putIntoMultiple(String key, List<String> values) {
		multiple.put(key, values);
	}
	
	public void putIntoMultiple(String key, String value) {
		if(!multiple.containsKey(key)) multiple.put(key, new ArrayList<>());
		multiple.get(key).add(value);
	}
	
	public void add(String item) {
		picture.add(item);
	}
}
