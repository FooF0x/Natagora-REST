package com.helmo.archi.google.googleuse.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface BasicService<Model, IdType> {
	
	List<Model> getAll();
	
	Model getById(IdType id);
	
	Model create(Model model);
	
	default List<Model> create(Model... models) {
		List<Model> rtn = new ArrayList<>();
		for (Model mod : models)
			rtn.add(create(mod));
		return rtn;
	}
	
	default List<Model> update(Model... models) {
		List<Model> rtn = new ArrayList<>();
		for (Model mod : models)
			rtn.add(update(mod));
		return rtn;
	}
	
	Model update(Model model);
	
	void delete(Model... models);
	
	void delete(Model model);
	
	void deleteById(IdType id);
}
