package com.helmo.archi.google.googleuse.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "attributes")
@Getter
@Setter
public class Attribute extends MongoIdentifiedModel {
	
	private String name;
	private List<Object> values;
}
