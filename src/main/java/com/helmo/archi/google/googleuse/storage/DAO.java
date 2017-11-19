package com.helmo.archi.google.googleuse.storage;

import java.util.List;

public interface DAO <Entity, IdType> {

	<T extends Entity> T find(IdType key);
	<T extends Entity> List<T> findAll();
	
	<T extends Entity> IdType save(T entity);
	
	<T extends Entity> void update(T entity);
	
	void delete(IdType key);
	
	boolean exist(IdType key);
}
