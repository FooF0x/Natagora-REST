package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Attribute;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttributeRepository extends MongoRepository<Attribute, Long> {
}
