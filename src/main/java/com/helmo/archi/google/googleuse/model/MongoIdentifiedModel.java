package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

public abstract class MongoIdentifiedModel {
	
	@Id
	@Getter @Setter
	private Long id;
}
