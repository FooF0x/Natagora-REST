package com.helmo.archi.google.googleuse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BasicController<Model> {
	
	@GetMapping
	List<Model> getAll();
	@GetMapping("/{id}")
	Model getOne(@PathVariable("id") long id);
	
	@PostMapping
	List<Model> create(@RequestBody Model... models);
	
	@PutMapping
	List<Model> update(@RequestBody Model... models);
	
	@DeleteMapping("/{id}")
	ResponseEntity deleteOne(@PathVariable("id") long id);
	@DeleteMapping
	ResponseEntity delete(@RequestBody Model... models);
}
