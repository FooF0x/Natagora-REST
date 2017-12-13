package com.helmo.archi.google.googleuse.model;

public interface AddableModel<Model> {
	
	/**
	 * Used to add the model with a custom id
	 *
	 * @return The object without the id
	 */
	Model getAddable();
}
