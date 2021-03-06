package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.model.BirdFinder;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.service.BirdService;
import com.helmo.archi.google.googleuse.service.ObservationService;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/birds")
public class BirdController implements BasicController<Bird> {
	
	private final BirdService brdSrv;
	private final ObservationService obsSrv;
	private final GoogleStorage storage;
	
	public BirdController(BirdService brdSrv, ObservationService obsSrv, GoogleStorage storage) {
		this.brdSrv = brdSrv;
		this.obsSrv = obsSrv;
		this.storage = storage;
	}
	
	@Override
	@GetMapping
	@Secured("ROLE_USER")
	public List<Bird> getAll() {
		return brdSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	@Secured("ROLE_USER")
	public Bird getOne(@PathVariable("id") long id) {
		return brdSrv.getById(id);
	}
	
	@GetMapping("/{one}/{two}")
	@Secured("ROLE_USER")
	public List<Bird> getRange(@PathVariable("one") long one, @PathVariable("two") long two) {
		if (two <= one) throw new IllegalArgumentException("Wrong args");
		return brdSrv.getRange(one, two);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_ADMIN")
	public ResponseEntity create(@RequestBody Bird... birds) {
		try {
			for(Bird bird : birds) { //For all the birds
				bird.getPicture().forEach( //Define the public links
					  p -> bird.getPublicLinks().add(
					  	  storage.getPublicLink(Paths.get(p))
					  )
				);
			}
			brdSrv.create(birds);
			return ResponseEntity.ok(brdSrv.create(birds));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_ADMIN")
	public ResponseEntity update(@RequestBody Bird... birds) {
		try {
			List<Bird> dbBirds = new ArrayList<>();
			for(Bird bird : birds) { //For all the birds
				Bird dbBird = brdSrv.getById(bird.getId());
				
				dbBird.setName(bird.getName() != null
					  ? bird.getName()
					  : dbBird.getName());
				dbBird.setDescription(bird.getDescription() != null
					  ? bird.getDescription()
					  : dbBird.getDescription());
				dbBird.setData(bird.getData() != null
					  ? bird.getData()
					  : dbBird.getData());
				dbBird.setPicture(bird.getPicture() != null
					  ? bird.getPicture()
					  : dbBird.getPicture());
				dbBird.setPublicLinks(bird.getPublicLinks() != null
					  ? bird.getPublicLinks()
					  : dbBird.getPublicLinks());
				
				if(bird.getPicture() != null && bird.getPublicLinks() != null)
					bird.getPicture().forEach( //Define the public links
						  p -> {
							  String publicLink = storage.getPublicLink(Paths.get(p));
							  if(!dbBird.getPublicLinks().contains(publicLink)) {
								  dbBird.getPublicLinks().add(publicLink);
							  }
						  }
					);
				dbBirds.add(dbBird);
			}
			return ResponseEntity.ok(brdSrv.update(dbBirds.toArray(new Bird[]{})));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@DeleteMapping("/{id}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity deleteOne(@PathVariable("id") Long id) {
		try {
			deleteDependencies(id);
			brdSrv.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.unprocessableEntity().body(ex.getMessage());
		}
	}
	
	@Override
	@DeleteMapping
	@Secured("ROLE_ADMIN")
	public ResponseEntity delete(Bird... birds) {
		try {
			Arrays.asList(birds).forEach(
				  o -> deleteDependencies(o.getId())
			);
			brdSrv.delete(birds);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.unprocessableEntity().body(ex.getMessage());
		}
	}
	
	private void deleteDependencies(Long id) {
		List<Observation> observations = obsSrv.getByBirdId(id);
		observations.forEach(
			  o -> o.setBirdId(0)
		);
		obsSrv.update(observations.toArray(new Observation[]{}));
	}
	
	@PostMapping("/helper")
	@Secured("ROLE_USER")
	public List<List<Object>> birdHelper(@RequestBody BirdFinder seed) {
		seed.processInput();
		
		//TODO Extract the database part in a stringItems method
		Map<String, String> stringItems = seed.getStringItems(); //Original Seed
		Map<String, List<Bird>> matchBirdString = new HashMap<>(); //Bird match the attribute
		Map<String, Map<Bird, Double>> processString = new HashMap<>();
		
		for (String key : stringItems.keySet()) {
			//Find birds inside Database
			matchBirdString.put(key, brdSrv.findSingleByArgs(key, stringItems.get(key)));
			
			double maxScore = 0;
			Map<Bird, Double> birdScore = new HashMap<>();
			for (Bird tempBird : matchBirdString.get(key)) { //For all the birds found for this value
				if (birdScore.containsKey(tempBird)) { //If already found
					Double current = birdScore.get(tempBird); //Get its score
					birdScore.replace(tempBird, current, ++current); //Its score +1
				} else  //If first time
					birdScore.put(tempBird, 1.0); //Create with one as score
				
				if (birdScore.get(tempBird) > maxScore) //Define max score
					maxScore = birdScore.get(tempBird);
			}
			processString.put(key, birdScore);
		}
		
		//TODO Extract the database part in a stringItems method
		Map<String, Long> longItems = seed.getLongItems(); //Original Seed
		Map<String, List<Bird>> matchBirdLong = new HashMap<>(); //Bird match the attribute
		Map<String, Map<Bird, Double>> processLong = new HashMap<>();
		for (String key : longItems.keySet()) {
			long searchValue = longItems.get(key);
			//Find birds inside Database
			matchBirdLong.put(key, brdSrv.findSingleByArgs(key, longItems.get(key)));
			
			processBird(key, searchValue, matchBirdLong, processLong);
			
		}
		
		//TODO Extract the database part in a stringItems method
		Map<String, Double> doubleItems = seed.getDoubleItems(); //Original Seed
		Map<String, List<Bird>> matchBirdDouble = new HashMap<>(); //Bird match the attribute
		Map<String, Map<Bird, Double>> processDouble = new HashMap<>();
		
		for (String key : doubleItems.keySet()) {
			double searchValue = doubleItems.get(key);
			//Find birds inside Database
			matchBirdDouble.put(key, brdSrv.findSingleByArgs(key, doubleItems.get(key)));
			
			processBird(key, searchValue, matchBirdDouble, processDouble);
		}
		
		Map<Bird, Double> fullScore = new HashMap<>();
		double maxValue = 0;
		maxValue = processFinalMap(maxValue, processString, fullScore);
		maxValue = processFinalMap(maxValue, processLong, fullScore);
		maxValue = processFinalMap(maxValue, processDouble, fullScore);
		
		List<List<Object>> finalScore = new ArrayList<>();
		for (Bird brd : fullScore.keySet()) {
			List<Object> temp = new ArrayList<>(
				  Arrays.asList(fullScore.get(brd) / maxValue, brd));
			finalScore.add(temp);
		}
		return finalScore;
	}
	
	@GetMapping("/names")
	@Secured("ROLE_USER")
	public List<String> getNames() {
		List<String> values = new ArrayList<>();
		brdSrv.getAll().forEach(
			  b -> values.add(b.getName())
		);
		return values;
	}
	
	private void processBird(String key, double searchValue, Map<String, List<Bird>> matchBirds, Map<String, Map<Bird, Double>> content) {
		double maxScore = 0;
		Map<Bird, Double> birdScore = new HashMap<>();
		
		for (Bird tempBird : matchBirds.get(key)) { //For all the birds found for this value
			double ratio = (double) ((Number) tempBird.get(key).get(0)).longValue() / searchValue;
			if (ratio > 1) ratio = 1 - (ratio % 1);
			if (birdScore.containsKey(tempBird)) { //If already found
				Double current = birdScore.get(tempBird); //Get its score
				birdScore.replace(tempBird, current,
					  current + ratio); //
			} else  //If first time
				birdScore.put(tempBird, ratio); //Create with one as score
			
			if (birdScore.get(tempBird) > maxScore) //Define max score
				maxScore = birdScore.get(tempBird);
		}
		content.put(key, birdScore);
	}
	
	private double processFinalMap(double maxValue, Map<String, Map<Bird, Double>> values, Map<Bird, Double> fullScore) {
		for (String key : values.keySet()) { //For all the values
			Map<Bird, Double> tempScore = values.get(key);
			for (Bird brd : tempScore.keySet()) { //For all the bird in the value
				Double replace;
				if (fullScore.containsKey(brd)) {
					Double current = fullScore.get(brd);
					fullScore.replace(brd, current,
						  replace = current + tempScore.get(brd));
				} else {
					fullScore.put(brd, replace = tempScore.get(brd));
				}
				
				if (replace > maxValue)
					maxValue = replace;
			}
		}
		return maxValue;
	}
}