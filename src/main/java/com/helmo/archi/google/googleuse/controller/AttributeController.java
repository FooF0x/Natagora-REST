package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Attribute;
import com.helmo.archi.google.googleuse.service.AttributeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attributes")
public class AttributeController implements BasicController<Attribute> {
	
	private final AttributeService attSrv;
	
	public AttributeController(AttributeService attSrv) {
		this.attSrv = attSrv;
	}
	
	@Override
	@GetMapping
	@Secured("ROLE_ADMIN")
	public List<Attribute> getAll() {
		return attSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	@Secured("ROLE_ADMIN")
	public Attribute getOne(@PathVariable("id") long id) {
		return attSrv.getById(id);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_ADMIN")
	public ResponseEntity create(@RequestBody Attribute... attributes) {
		try {
			return ResponseEntity.ok(attSrv.create(attributes));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_ADMIN")
	public ResponseEntity update(@RequestBody Attribute... attributes) {
		try {
			return ResponseEntity.ok(attSrv.update(attributes));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@DeleteMapping("/{id}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity deleteOne(@PathVariable("id") long id) {
		attSrv.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	@Override
	@DeleteMapping
	@Secured("ROLE_ADMIN")
	public ResponseEntity delete(@RequestBody Attribute... attributes) {
		attSrv.delete(attributes);
		return ResponseEntity.ok().build();
	}
}
