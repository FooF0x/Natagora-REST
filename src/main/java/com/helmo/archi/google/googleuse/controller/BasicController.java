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
	ResponseEntity create(@RequestBody Model... models);
	
	@PutMapping
	ResponseEntity update(@RequestBody Model... models);
	
	@DeleteMapping("/{id}")
	ResponseEntity deleteOne(@PathVariable("id") Long id);
	
	@DeleteMapping
	ResponseEntity delete(@RequestBody Model... models);
}
