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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/observations")
public class ObservationController implements BasicController<Observation> {
	
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
	
	@Override
	@GetMapping
	public List<Observation> getAll() {
		return obsSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	public Observation getOne(@PathVariable("id") long id) {
		return obsSrv.getById(id);
	}
	
	@Override
	@PostMapping
	public List<Observation> create(@RequestBody Observation... observs) {
		//Analyse the pics
		List<Observation> rtn = new ArrayList<>();
		Observation added;
		try {
			for(Observation obs : observs) {
				SafeSearchAnnotation safe = vision.safeSearchAnalyse(
						obs.getOnlinePath());
				obs.setAnalyseResult(safe.toString());
				rtn.add(added = obsSrv.create(obs));
				
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
					notSrv.create(NotificationBuilder.getDefaultNotification( //Send a notification
							"Problème avec une observation",
							message,
							added
					));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	private Map<String, String> translateSafeSearch(SafeSearchAnnotation safe) {
		Map<String, String> rtn = new HashMap<>();
		
		rtn.put("adult", translate.simpleTranslateFromENToFR(safe.getAdult().name()));
		rtn.put("medial", translate.simpleTranslateFromENToFR(safe.getMedical().name()));
		rtn.put("violence", translate.simpleTranslateFromENToFR(safe.getViolence().name()));
		rtn.put("spoof", translate.simpleTranslateFromENToFR(safe.getSpoof().name()));
		
		
		return rtn;
	}
	
	@Override
	public List<Observation> update(Observation... observations) {
		return obsSrv.update(observations);
	}
	
	@Override
	public ResponseEntity delete(Observation... observations) {
		return ResponseEntity.badRequest().build();
	}
	
	@Override
	@DeleteMapping("/{id}")
	@Secured("ROLE_USER")
	public ResponseEntity deleteOne(@PathVariable("id") long id) {
//		Observation obs = obsSrv.findOne(id);
//		repSrv.deleteByObservation(obs); //Delete all reports
//		cmtSrv.deleteByObservation(obs); //Delete all comments
//		notSrv.deleteByObservation(obs); //Delete all notifications
		obsSrv.deleteById(id);
		return ResponseEntity.badRequest().build();
	}
	
}
