package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.model.BirdFinder;
import com.helmo.archi.google.googleuse.service.BirdService;
import com.helmo.archi.google.googleuse.service.NextSequenceService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/birds")
public class BirdController {
	
	private final BirdService brdSrv;
	private final NextSequenceService nextSeq;
	
	public BirdController(BirdService brdSrv, NextSequenceService nxtSeq) {
		this.brdSrv = brdSrv;
		this.nextSeq = nxtSeq;
	}
	
	@GetMapping
	@Secured("ROLE_USER")
	public List<Bird> getBird() {
		return brdSrv.getAll();
	}
	
	@GetMapping("/{id}")
	@Secured("ROLE_USER")
	public Bird getSingleBird(@PathVariable("id") long id) {
		return brdSrv.getById(id);
	}
	
	@PostMapping
	@Secured("ROLE_ADMIN")
	public void postBird(@RequestBody Bird bird) {
		bird.setId(nextSeq.getNextSequence("birds"));
		brdSrv.create(bird);
	}
	
	@PutMapping
	@Secured("ROLE_ADMIN")
	public void updateBird(@RequestBody Bird bird) {
		brdSrv.update(bird);
	}
	
	@DeleteMapping("/{id}")
	@Secured("ROLE_ADMIN")
	public void deleteBird(@PathVariable("id") long id) {
		brdSrv.deleteById(id);
	}
	
	@PostMapping("/helper")
	@Secured("ROLE_USER")
	public Set<Bird> birdHelper(@RequestBody BirdFinder seed) {
		seed.processInput();
		Set<Bird> rtn = new HashSet<>();
		for (String key : seed.getSingle().keySet())
			rtn.addAll(brdSrv.findSingleByArgs(key, seed.getSingle().get(key)));
		return rtn;
		
	}
	
	private void trash() {
//		Map<Bird, Double> rtn = new HashMap<>();
//
//		//TODO Extract the database part in a single method
//		Map<String, String> tempSingle = seed.getSingle(); //Original Seed
//		Map<String, List<Bird>> tempBirdsString = new HashMap<>(); //Bird match the attribute
//		Map<String, Map<Bird, Double>> processString = new HashMap<>(); //
//		for(String key : tempSingle.keySet()) {
//			tempBirdsString.put(key, brdSrv.findSingleByArgs(key, tempSingle.get(key)));
//			//TODO Process the result
//			Map<Bird, Double> tempScore = new HashMap<>();
//			for(String key2 : tempBirdsString.keySet()) {
//				for(Bird tempBird : tempBirdsString.get(key2)) {
//					if(tempScore.containsKey(tempBird)) {
//						Double current = tempScore.get(tempBird);
//						tempScore.replace(tempBird, current, ++current);
//					} else {
//						tempScore.put(tempBird, 1.0);
//					}
//				}
//			}
//			//Get the max
//			double maxScore = 0;
//			for (Bird tempBird : tempScore.keySet()) {
//				if(tempScore.get(tempBird) > maxScore)
//					maxScore = tempScore.get(tempBird);
//			}
//
//			//Score x/1
//			Map<Bird, Double> finalScore = new HashMap<>();
//			for(Bird tempBird : tempScore.keySet()) {
//				finalScore.put(tempBird, tempScore.get(tempBird) / maxScore);
//			}
//			processString.put(key, finalScore);
//		}
//		Map<String, Long> tempLong = seed.getLongItems();
//		Map<String, List<Bird>> tempBirdsLong = new HashMap<>();
//		Map<String, List<Map<Bird, Double>>> processLong = new HashMap<>();
//		for(String key : tempLong.keySet()){
//			tempBirdsLong.put(key, brdSrv.findSingleByArgs(key, tempSingle.get(key)));
//			//TODO Process the result
//			Map<Bird, Double> tempScore = new HashMap<>();
//			for(String key2 : tempBirdsLong.keySet()) {
//				for(Bird tempBird : tempBirdsString.get(key2)) {
//					if(tempScore.containsKey(tempBird)) {
//						Double current = tempScore.get(tempBird);
//						tempScore.replace(tempBird, current, ++current);
//					} else {
//						tempScore.put(tempBird, 1.0);
//					}
//				}
//			}
//			//Get the max
//			double maxScore = 0;
//			for (Bird tempBird : tempScore.keySet()) {
//				if(tempScore.get(tempBird) > maxScore)
//					maxScore = tempScore.get(tempBird);
//			}
//
//			//Score x/1
//			Map<Bird, Double> finalScore = new HashMap<>();
//			for(Bird tempBird : tempScore.keySet()) {
//				finalScore.put(tempBird, tempScore.get(tempBird) / maxScore);
//			}
//			processString.put(key, finalScore);
//		}
//		Map<String, Double> tempDouble = seed.getDoubleItems();
//		Map<String, List<Bird>> tempBirdsDouble = new HashMap<>();
//		Map<String, List<Map<Bird, Double>>> processDouble = new HashMap<>();
//		for(String key : tempDouble.keySet()){
//			tempBirdsDouble.put(key, brdSrv.findSingleByArgs(key, tempSingle.get(key)));
//			//TODO Process the result
//		}
//
//		//TODO Process the maps
	
	}

//	<X extends java.lang> Map<String, Map<Bird, Double>> processAttribute(Map<String, X> seed) {
//		Map<String, List<Bird>> birdsFound = new HashMap<>(); //Bird match the attribute
//		Map<String, Map<Bird, Double>> process = new HashMap<>(); //
//		for(String key : seed.keySet()) {
//			birdsFound.put(key, brdSrv.<X>findSingleByArgs(key, seed.get(key)));
//			//TODO Process the result
//			Map<Bird, Double> tempScore = new HashMap<>();
//			for(String key2 : birdsFound.keySet()) {
//				for(Bird tempBird : birdsFound.get(key2)) {
//					if(tempScore.containsKey(tempBird)) {
//						Double current = tempScore.get(tempBird);
//						tempScore.replace(tempBird, current, ++current);
//					} else {
//						tempScore.put(tempBird, 1.0);
//					}
//				}
//			}
//			//Get the max
//			double maxScore = 0;
//			for (Bird tempBird : tempScore.keySet()) {
//				if(tempScore.get(tempBird) > maxScore)
//					maxScore = tempScore.get(tempBird);
//			}
//
//			//Score x/1
//			Map<Bird, Double> finalScore = new HashMap<>();
//			for(Bird tempBird : tempScore.keySet()) {
//				finalScore.put(tempBird, tempScore.get(tempBird) / maxScore);
//			}
//			process.put(key, finalScore);
//		}
//
//		return process;
//	}
}
