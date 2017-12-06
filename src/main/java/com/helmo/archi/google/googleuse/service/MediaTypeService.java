package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.MediaType;
import com.helmo.archi.google.googleuse.repository.MediaTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MediaTypeService implements BasicService<MediaType, Long> {
	
	private final MediaTypeRepository medRepo;
	
	public MediaTypeService(MediaTypeRepository medRepo) {
		this.medRepo = medRepo;
	}
	
	@Override
	public List<MediaType> getAll() {
		return medRepo.findAll();
	}
	
	@Override
	public MediaType getById(Long id) {
		return medRepo.findOne(id);
	}
	
	public MediaType getByName(String name) {
		return medRepo.findByName(name);
	}
	
	@Override
	public MediaType create(MediaType mediaType) {
		MediaType toCreate = new MediaType();
		mediaType.setName(mediaType.getName());
		return medRepo.save(toCreate);
	}
	
	@Override
	public MediaType update(MediaType mediaType) {
		return medRepo.save(mediaType);
	}
	
	@Override
	public List<MediaType> update(MediaType... mediaTypes) {
		return medRepo.save(Arrays.asList(mediaTypes));
	}
	
	@Override
	public void delete(MediaType... mediaTypes) {
		medRepo.delete(Arrays.asList(mediaTypes));
	}
	
	@Override
	public void delete(MediaType mediaType) {
		medRepo.delete(mediaType);
	}
	
	@Override
	public void deleteById(Long id) {
		medRepo.delete(id);
	}
}
