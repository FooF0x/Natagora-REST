package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

public class MongoIdentifiedModel {
	
	@Id
	@Getter @Setter
	private String id;
}
