package com.helmo.archi.google.googleuse.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BasicController<Model> {
	
	List<Model> getAll();
	Model getOne(long id);
	
	List<Model> create(Model... models);
	
	List<Model> update(Model... models);
	
	ResponseEntity deleteOne(long id);
	ResponseEntity delete(Model... models);
}
