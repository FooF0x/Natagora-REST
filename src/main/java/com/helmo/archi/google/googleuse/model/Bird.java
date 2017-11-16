package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class Bird extends IdentifiedModel {
	
	public static final String NAME = "name"; //Used with Datastore
	public static final String DESCRIPTION = "description";
	public static final String DATAS = "datas";
	
	private String name;
	private String description;
	private Map<String, String> datas;
	
	public Bird() {}
	
	public String get(String key) {
		return datas.get(key);
	}
	
	public void put(String key, String value) {
		datas.put(key, value);
	}
}
