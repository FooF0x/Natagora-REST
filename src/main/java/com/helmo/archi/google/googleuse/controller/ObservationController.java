package com.helmo.archi.google.googleuse.controller;

import com.google.cloud.vision.v1.Likelihood;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.helmo.archi.google.googleuse.ml.GoogleTranslate;
import com.helmo.archi.google.googleuse.ml.GoogleVision;
import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.service.CommentService;
import com.helmo.archi.google.googleuse.service.NotificationService;
import com.helmo.archi.google.googleuse.service.ObservationService;
import com.helmo.archi.google.googleuse.service.ReportService;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.sql.Timestamp;
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
			//Save the to SQL
			//Define Notification
			if(safe.getAdultValue() >= 2
					|| safe.getMedicalValue() >= 2
					|| safe.getViolenceValue() >= 2){
				//Translate the result
				Map<String, String> rlt = translateSafeSearch(safe);
				//Send a notification
				Notification notif = new Notification();
				notif.setDate(new Timestamp(new Date().getTime()));
				notif.setCaption("Problème avec une observation");
				notif.setDescription(String.format(
						"Analyse de l'image :\n" +
								"Violance : %s\n" +
								"Adulte : %s\n" +
								"Medical : %s\n" +
								"Canular : %s",
						rlt.get("violence"), rlt.get("adult"), rlt.get("medical"), rlt.get("spoof")
				));
				notif.setObservation(obsSrv.save(obs));
				notif.setStatus(false);
				notSrv.save(notif);
			} else
				obsSrv.save(obs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Map<String, String> translateSafeSearch(SafeSearchAnnotation safe) {
		Map<String, String> rtn = new HashMap<>();
		rtn.put("adult", lightTranslate(safe.getAdult()));
		rtn.put("medial", lightTranslate(safe.getMedical()));
		rtn.put("violence", lightTranslate(safe.getViolence()));
		rtn.put("spoof", lightTranslate(safe.getSpoof()));
		return rtn;
	}
	
	private String lightTranslate(Likelihood item) {
		switch (item) {
			case UNRECOGNIZED: return "Non reconnu";
			case VERY_UNLIKELY: return "Très improbable";
			case UNLIKELY: return "Improbable";
			case POSSIBLE: return "Possible";
			case LIKELY: return "Probable";
			case VERY_LIKELY: return "Très probable";
			default : return "Inconnu";
		}
	}
	
	@DeleteMapping("/{id}")
	public void deleteObservation(@PathVariable("id") Long id) {
//		repSrv.deleteAll(repSrv.findByObservation(id)); //Delete all reports
		//Delete all comments
		//Delete all notifications
		obsSrv.deleteById(id);
	}
	
}
