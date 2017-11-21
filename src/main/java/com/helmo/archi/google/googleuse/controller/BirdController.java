package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.service.BirdService;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/birds")
public class BirdController {
	
	private final BirdService brdSrv;
	
	public BirdController(BirdService brdSrv) {
		this.brdSrv = brdSrv;
	}
	
	@GetMapping
	public List<Bird> getBirds() {
		return brdSrv.getBirds();
	}
	
	private Bird jsonToBird(String json) { //TODO Check if there are Name and Desc at least
		JsonParser parser = JsonParserFactory.getJsonParser();
		Map<String, Object> result = parser.parseMap(json);
		
		Bird bird = new Bird();
		if(result.containsKey("id"))
			bird.setId((long) result.remove("id"));
		
		bird.setName((String) result.remove("name"));
		bird.setDescription((String) result.remove("description"));
		bird.setPicture(new ArrayList<>());
		
		for(Object obj : parser.parseList("picture"))
			bird.add((String) obj);
		
		result.remove("picture");
		bird.setData(new HashMap<>());
		
		for(String key : result.keySet()) {
			bird.putIntoData(key, (String) result.get(key));
		}
		return bird;
	}
	
//	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	@PostMapping()
	@ResponseBody
	public void createBird(@RequestBody Bird bird) {
		brdSrv.createBird(bird);
	}
	
	@GetMapping("/{id}")
	public Bird getById(@PathVariable("id") long id) {
		return brdSrv.getById(id);
	}
	
	@PutMapping
	@ResponseBody
	public void updateBird(@RequestBody Bird bird) {
		brdSrv.update(bird);
	}
	
	@DeleteMapping("/{id}")
	public void deleteBird(@PathVariable("id") Long id) {
		brdSrv.deleteBird(id);
	}
}
