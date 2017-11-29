package com.helmo.archi.google.googleuse.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BasicService<Model, IdType> {
	
	List<Model> getAll();
	Model getById(IdType id);
	
	List<Model> create(Model... models);
	Model create(Model model);
	
	List<Model> update(Model... models);
	Model update(Model model);
	
	List<Model> delete(Model... models);
	Model delete(Model model);
	Model deleteById(IdType id);
}
