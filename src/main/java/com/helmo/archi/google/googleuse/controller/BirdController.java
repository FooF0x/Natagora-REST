package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.service.BirdService;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


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
	
	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	public void createBird(HttpEntity<String> httpEntity) {
		String json = httpEntity.getBody(); //TODO Check if there are Name and Desc at least
		
		JsonParser parser = JsonParserFactory.getJsonParser();
		Map<String, Object> result = parser.parseMap(json);
		
		Bird bird = new Bird();
		bird.setName((String) result.get("name"));
		bird.setDescription((String) result.get("description"));
		bird.setDatas(new HashMap<>());
		
		for(String key : result.keySet()) {
			bird.put(key, (String) result.get(key));
		}
		
		brdSrv.createBird(bird);
	}
}
