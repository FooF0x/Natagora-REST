package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.repository.MediaTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class MediaTypeService {
	
	private final MediaTypeRepository medRepo;
	
	public MediaTypeService(MediaTypeRepository medRepo) {
		this.medRepo = medRepo;
	}
}
