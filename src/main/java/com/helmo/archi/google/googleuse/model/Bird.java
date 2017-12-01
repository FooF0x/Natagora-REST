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
public class Bird extends MongoIdentifiedModel implements AddableModel<Bird> {
	
	@Indexed(unique = true)
	private String name;
	private String description;
	
	private List<String> picture;
	private Map<String, List<String>> data;
	
	public Bird() {
	}
	
	@Override
	public Bird getAddable() {
		Bird bird = new Bird();
		bird.setName(this.name);
		bird.setDescription(this.description);
		bird.setData(this.data);
		bird.setPicture(this.picture);
		return bird;
	}
	
	@Override
	public String toString() {
		return String.format(
			  "BIRD [id=%s, name=%s ]\n\t[Picture : %d]\n\t[Multiple : %d]",
			  getId(), name, picture.size(), data.size()
		);
	}
}
