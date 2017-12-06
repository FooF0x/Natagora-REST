package com.helmo.archi.google.googleuse.tools;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.helmo.archi.google.googleuse.ml.GoogleTranslate;
import com.helmo.archi.google.googleuse.ml.GoogleVision;
import com.helmo.archi.google.googleuse.model.MediaType;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.service.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class ObservationChecker {
	
	private final MediaType picType;
	private final MediaType vidType;
	private final MediaType audType;
	private final MediaType notType;
	
	private final ObservationService obsSrv;
	private final NotificationService notSrv;
	private final SessionService sesSrv;
	private final BirdService brdSrv;
	
	private final GoogleVision vision;
	private final GoogleTranslate translate;
	
	private final Pattern picPattern;
	private final Pattern vidPattern;
	private final Pattern audPattern;
	
	public ObservationChecker(ObservationService obsSrv, NotificationService notSrv, MediaTypeService medSrv,
	                          GoogleVision vision, GoogleTranslate translate, Environment env, SessionService sesSrv,
	                          BirdService brdSrv) {
		this.obsSrv = obsSrv;
		this.notSrv = notSrv;
		this.sesSrv = sesSrv;
		this.brdSrv = brdSrv;
		
		this.vision = vision;
		this.translate = translate;
		
		picType = medSrv.getByName(env.getProperty("data.mediaTypes.picture"));
		vidType = medSrv.getByName(env.getProperty("data.mediaTypes.video"));
		audType = medSrv.getByName(env.getProperty("data.mediaTypes.audio"));
		notType = medSrv.getByName(env.getProperty("data.mediaTypes.nothing"));
		
		picPattern = getPatternFromEnvironment(env.getProperty("storage.allowedPicExt"));
		vidPattern = getPatternFromEnvironment(env.getProperty("storage.allowedVidExt"));
		audPattern = getPatternFromEnvironment(env.getProperty("storage.allowedAudExt"));
		
	}
	
	private Pattern getPatternFromEnvironment(String base) {
		StringBuilder strPattern = new StringBuilder();
		for(String str : base.split(","))
			strPattern.append("(?=.*.").append(str).append(")");
		return Pattern.compile(strPattern.toString());
	}
	
	public List<Observation> observationAdder(Session session, Observation... observations) {
		List<Observation> rtn = new ArrayList<>();
		Observation added;
		boolean sesOk = (session != null);
		Session dbSes = session;
		if(sesOk) dbSes = sesSrv.getById(session.getId());
		
		try {
			for (Observation obs : observations) {
				/* CHECK FOR SESSION */
				if(sesOk) obs.setSession(dbSes);
				else if(obs.getSession() == null) throw new NullPointerException("No session define");
				
				/* DEFINE IS THE BIRD EXIST (Because no verification from MySQL)*/
				if(!brdSrv.exist(obs.getBirdId())) throw new IllegalArgumentException("Bird ID not correct");
				
				/* DEFINE MEDIA TYPE */
				defineMediaType(obs);
				
				/* ADD TO DATABASE*/
				added = obsSrv.create(obs);
				
				//TODO MAYBE, add to MongoDB for geographical research
				
				/* ANALYSE BASED ON TYPE*/
				analyseMedia(added);
				
				rtn.add(obsSrv.update(added)); //Because analyseMedia doesn't update database
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	private void defineMediaType(Observation obs) {
		if(obs.getOnlinePath() != null && obs.getOnlinePath().trim().length() > 0) {
			String path = obs.getOnlinePath();
			if (picPattern.matcher(path).find()) {
				obs.setMediaType(picType);
			} else if (vidPattern.matcher(path).find()) {
				obs.setMediaType(picType);
			} else if (audPattern.matcher(path).find()) {
				obs.setMediaType(audType);
			}
		} else {
			obs.setMediaType(notType);
		}
	}
	
	/**
	 * Analyse the obs and DOESN'T update the observation
	 * @param obs
	 * @throws Exception
	 */
	private void analyseMedia(Observation obs) throws Exception {
		if(obs.getMediaType().equals(picType)) {
			AnnotateImageResponse analyse = vision.simpleAnalyse(
				  Paths.get(obs.getOnlinePath()));
			obs.setAnalyseResult(analyse.getSafeSearchAnnotation().toString());
			checkNotification(obs, analyse);
		} if(obs.getMediaType().equals(vidType) || obs.getMediaType().equals(audType)){ //TODO Add Google Cloud Video Intelligence
			obs.setAnalyseResult("ANALYSE NOT AVAILABLE\nAnalyse only available for : pictures");
		}
	}
	
	public List<Observation> observationUpdater(Observation... observations) {
		List<Observation> toUpdate = new ArrayList<>();
		try {
			for (Observation obs : observations) {
				Observation oldObs = obsSrv.getById(obs.getId());
				
				/* CHECK THE PICTURE */
				String newPath = obs.getOnlinePath();
				String oldPath = oldObs.getOnlinePath();
				if(newPath != null && newPath.trim().length() > 0) { //That means there's something
					if(oldPath != null && oldPath.trim().length() > 0) {
						if(!oldPath.equals(newPath)) { //If not equals, there's something different
							defineMediaType(obs);
							analyseMedia(obs);
						}
					} else { //If old was null or empty, but not the new one, that means there's something new
						defineMediaType(obs);
						analyseMedia(obs);
					}
				}
				
				/* CHECK THE BIRD */
				if(oldObs.getBirdId() != obs.getBirdId())
					if(!brdSrv.exist(obs.getBirdId())) throw new IllegalArgumentException("Bird ID not correct");
				
				toUpdate.add(obsSrv.update(obs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toUpdate;
	}
	
	public void checkNotification(Observation obs, AnnotateImageResponse analyse) {
		
		SafeSearchAnnotation safe = analyse.getSafeSearchAnnotation();
		List<EntityAnnotation> labels = analyse.getLabelAnnotationsList();
		//Define Notification
		if (safe.getAdultValue() >= 2
			  || safe.getMedicalValue() >= 2
			  || safe.getViolenceValue() >= 2) {
			Map<String, String> rlt = translateSafeSearch(safe); //Translate the result
			String message = String.format(
				  "Analyse de l'image :\n" +
						"Violance : %s\n" +
						"Adulte   : %s\n" +
						"Medical  : %s\n" +
						"Canular  : %s",
				  rlt.get("violence"), rlt.get("adult"), rlt.get("medical"), rlt.get("spoof")
			);
			notSrv.create(NotificationBuilder.getDefaultNotification( //Send a notification
				  "Problème avec une observation",
				  message,
				  obs
			));
			obs.setValidation(false);
			obsSrv.update(obs);
		} else if(!containsNotCaseSensitive(labels, "bird")) {
			
			StringBuilder message = new StringBuilder("Analyse de l'image : \n");
			for(EntityAnnotation entity : labels)
				message.append(
					  translate.simpleTranslateFromENToFR(entity.getDescription()))
					  .append(" : ").append(entity.getScore()).append("\n");
			
			notSrv.create(NotificationBuilder.getDefaultNotification( //Send a notification
				  "Aucun oiseau détecté",
				  message.toString(),
				  obs
			));
			obs.setValidation(false);
			obsSrv.update(obs);
		}
	}
	
	private boolean containsNotCaseSensitive(List<EntityAnnotation> entities, String key) {
		boolean rtn = false;
		String lowerKey = key.toLowerCase();
		for(EntityAnnotation entity : entities)
			if(entity.getDescription().toLowerCase().contains(lowerKey)) {
				rtn = true;
				break;
			}
		return rtn;
	}
	
	private Map<String, String> translateSafeSearch(SafeSearchAnnotation safe) {
		Map<String, String> rtn = new HashMap<>();
		
		rtn.put("adult", translate.simpleTranslateFromENToFR(safe.getAdult().name()));
		rtn.put("medical", translate.simpleTranslateFromENToFR(safe.getMedical().name()));
		rtn.put("violence", translate.simpleTranslateFromENToFR(safe.getViolence().name()));
		rtn.put("spoof", translate.simpleTranslateFromENToFR(safe.getSpoof().name()));
		
		
		return rtn;
	}
}
