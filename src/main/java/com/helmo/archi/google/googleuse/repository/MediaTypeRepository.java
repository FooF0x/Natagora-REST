package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaTypeRepository extends JpaRepository<MediaType, Long> {
	MediaType findByName(String name);
}
