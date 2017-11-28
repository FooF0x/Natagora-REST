package com.helmo.archi.google.googleuse.controller;

import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.helmo.archi.google.googleuse.ml.GoogleTranslate;
import com.helmo.archi.google.googleuse.ml.GoogleVision;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.service.CommentService;
import com.helmo.archi.google.googleuse.service.NotificationService;
import com.helmo.archi.google.googleuse.service.ObservationService;
import com.helmo.archi.google.googleuse.service.ReportService;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;
import com.helmo.archi.google.googleuse.tools.NotificationBuilder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/observations")
public class ObservationController {
	
	private final ObservationService obsSrv;
	private final NotificationService notSrv;
	private final ReportService repSrv;
	private final CommentService cmtSrv;
	private final GoogleStorage storage;
	private final GoogleVision vision;
	private final GoogleTranslate translate;
	
	public ObservationController(ObservationService obsSrv, NotificationService notSrv, ReportService repSrv,
	                             CommentService cmtSrv, GoogleStorage storage, GoogleVision vision, GoogleTranslate translate) {
		this.obsSrv = obsSrv;
		this.notSrv = notSrv;
		this.repSrv = repSrv;
		this.cmtSrv = cmtSrv;
		this.storage = storage;
		this.vision = vision;
		this.translate = translate;
	}
	
	@GetMapping
	public List<Observation> getObservations() {
		return obsSrv.getObservations();
	}

//	@GetMapping("/{id}")
//	public List<Observation> getObservations(@PathVariable("id") long id) {
//		Observation obs = obsSrv.findOne(id);
////		if(obs.getOnlinePath())
//		return obsSrv.getObservations();
//	}
	
	@PostMapping
	public void createObservation(@RequestBody Observation obs) {
		//Analyse the pics
		try {
			SafeSearchAnnotation safe = vision.safeSearchAnalyse(
				  obs.getOnlinePath());
			obs.setAnalyseResult(safe.toString());
			//Define Notification
			if (safe.getAdultValue() >= 2
				  || safe.getMedicalValue() >= 2
				  || safe.getViolenceValue() >= 2) {
				Map<String, String> rlt = translateSafeSearch(safe); //Translate the result
				String message = String.format(
					  "Analyse de l'image :\n" +
							"Violance : %s\n" +
							"Adulte : %s\n" +
							"Medical : %s\n" +
							"Canular : %s",
					  rlt.get("violence"), rlt.get("adult"), rlt.get("medical"), rlt.get("spoof")
				);
				notSrv.save(NotificationBuilder.getDefaultNotification( //Send a notification
					  "Problème avec une observation",
					  message,
					  obsSrv.save(obs)
				));
			} else
				obsSrv.save(obs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Map<String, String> translateSafeSearch(SafeSearchAnnotation safe) {
		Map<String, String> rtn = new HashMap<>();
		
		rtn.put("adult", translate.simpleTranslateFromENToFR(safe.getAdult().name()));
		rtn.put("medial", translate.simpleTranslateFromENToFR(safe.getMedical().name()));
		rtn.put("violence", translate.simpleTranslateFromENToFR(safe.getViolence().name()));
		rtn.put("spoof", translate.simpleTranslateFromENToFR(safe.getSpoof().name()));
		
		
		return rtn;
	}
	
	@DeleteMapping("/{id}")
	@Secured("ROLE_USER")
	public void deleteObservation(@PathVariable("id") long id) {
//		Observation obs = obsSrv.findOne(id);
//		repSrv.deleteByObservation(obs); //Delete all reports
//		cmtSrv.deleteByObservation(obs); //Delete all comments
//		notSrv.deleteByObservation(obs); //Delete all notifications
		obsSrv.deleteById(id);
	}
	
}
