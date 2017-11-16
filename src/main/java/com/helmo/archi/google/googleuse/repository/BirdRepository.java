package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Bird;

public interface BirdRepository {
	long save(Bird bird);
	Bird getById(long id);
}
