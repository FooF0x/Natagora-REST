package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Attribute;
import com.helmo.archi.google.googleuse.repository.AttributeRepository;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AttributeService implements AccessRange<Attribute, Long> {
	
	private final AttributeRepository attRepo;
	private final NextSequenceService nextSeq;
	private final MongoTemplate monqoTemplate;
	private final Environment env;
	
	private final String COLLECTION_NAME;
	
	public AttributeService(AttributeRepository attRepo, NextSequenceService nextSeq,
	                        MongoTemplate monqoTemplate, Environment env) {
		this.attRepo = attRepo;
		this.nextSeq = nextSeq;
		this.monqoTemplate = monqoTemplate;
		this.env = env;
		COLLECTION_NAME = env.getProperty("mongodb.attributes");
	}
	
	@Override
	public List<Attribute> getAll() {
		return attRepo.findAll();
	}
	
	@Override
	public Attribute getById(Long id) {
		return attRepo.findOne(id);
	}
	
	@Override
	public Attribute create(Attribute toSave) {
		Attribute att = new Attribute();
		att.setId(nextSeq.getNextSequence(COLLECTION_NAME));
		att.setName(toSave.getName());
		att.setValues(toSave.getValues());
		return attRepo.insert(att);
	}
	
	@Override
	public Attribute update(Attribute toUpdate) {
		Attribute attribute = attRepo.findOne(toUpdate.getId());
		attribute.setId(toUpdate.getId());
		attribute.setName(toUpdate.getName());
		attribute.setValues(toUpdate.getValues());
		
		attRepo.delete(toUpdate.getId());
		return attRepo.insert(attribute);
	}
	
	@Override
	public void delete(Attribute... attributes) {
		attRepo.delete(Arrays.asList(attributes));
	}
	
	@Override
	public void delete(Attribute attribute) {
		attRepo.delete(attribute);
	}
	
	@Override
	public void deleteById(Long id) {
		attRepo.delete(id);
	}
}
