package com.helmo.archi.google.googleuse.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "birds")
@Getter
@Setter
public class Bird extends MongoIdentifiedModel {
	
	@Indexed(unique = true)
	private String name;
	private String description;
	
	private List<String> picture;
	private List<String> publicLinks;
	private Map<String, List<Object>> data;
	
	public Bird() {
	}
	
	public List<Object> get(String key) {
		return data.get(key);
	}
	
	@Override
	public String toString() {
		return String.format(
			  "BIRD [id=%s, name=%s ]\n\t[Picture : %d]\n\t[Data : %d]",
			  getId(), name, picture.size(), data.size()
		);
	}
}
