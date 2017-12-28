package com.helmo.archi.google.googleuse.tools;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.helmo.archi.google.googleuse.ml.GoogleTranslate;
import com.helmo.archi.google.googleuse.ml.GoogleVision;
import com.helmo.archi.google.googleuse.model.*;
import com.helmo.archi.google.googleuse.service.*;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//There's no place like 127.0.0.1

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
	private final GoogleStorage storage;
	
	private final FileNameExtensionFilter picFilter;
	private final FileNameExtensionFilter vidFilter;
	private final FileNameExtensionFilter audFilter;
	
	private final NotificationStatus PENDING_STATUS;
	
	public ObservationChecker(ObservationService obsSrv, NotificationService notSrv, MediaTypeService medSrv,
	                          NotificationStatusService statusSrv, GoogleVision vision, GoogleTranslate translate,
	                          Environment env, SessionService sesSrv, BirdService brdSrv, GoogleStorage storage) {
		this.obsSrv = obsSrv;
		this.notSrv = notSrv;
		this.sesSrv = sesSrv;
		this.brdSrv = brdSrv;
		
		this.vision = vision;
		this.translate = translate;
		this.storage = storage;
		
		picType = medSrv.getByName(env.getProperty("data.mediaTypes.picture"));
		vidType = medSrv.getByName(env.getProperty("data.mediaTypes.video"));
		audType = medSrv.getByName(env.getProperty("data.mediaTypes.audio"));
		notType = medSrv.getByName(env.getProperty("data.mediaTypes.nothing"));
		
		picFilter = new FileNameExtensionFilter("Picture Files",
			  env.getProperty("storage.allowedPicExt").split(","));
		vidFilter = new FileNameExtensionFilter("Video Files",
			  env.getProperty("storage.allowedVidExt").split(","));
		audFilter = new FileNameExtensionFilter("Audio Files",
			  env.getProperty("storage.allowedAudExt").split(","));
		
		PENDING_STATUS = statusSrv.findByName("PENDING");
		
	}
	
	private boolean acceptedPictures(File file) {
		return picFilter.accept(file);
	}
	
	private boolean acceptedVideos(File file) {
		return vidFilter.accept(file);
	}
	
	private boolean acceptedAudios(File file) {
		return audFilter.accept(file);
	}
	
	public List<Observation> observationAdder(Session session, Observation... observations) throws Exception {
		List<Observation> rtn = new ArrayList<>();
		List<Notification> notifications = new ArrayList<>();
		Observation added;
		boolean sesOk = (session != null);
		Session dbSes = session;
		if (sesOk) dbSes = sesSrv.getById(session.getId());
		
		for (Observation obs : observations) {
			
			/* CHECK FOR SESSION */
			if (sesOk) obs.setSession(dbSes);
			else if (obs.getSession() == null) throw new NullPointerException("No session define");
			
			/* ADD TO DATABASE (to have the ID) */
			added = obsSrv.create(obs); //TODO MAYBE, add to MongoDB for geographical research
			
			/* DEFINE IS THE BIRD EXIST (Because no verification from MySQL)*/
			/* If equals 0, that means no bird known*/
			checkBirdId(notifications, added);
			
			try {
				/* DEFINE MEDIA TYPE IN CASE OF NULL OR WRONG */
				defineMediaType(added);
				definePublicLink(added);
			} catch (IllegalArgumentException ex) {
				notifications.add(NotificationBuilder.getDefaultNotification(
					  "Format du média invalide",
					  "Le format du média de l'observation est invalide : \n" + added.getOnlinePath(),
					  PENDING_STATUS,
					  added));
			}
			
			/* ANALYSE BASED ON TYPE*/
			analyseMedia(added, notifications);
			
			obsSrv.update(added); //Because analyseMedia doesn't update database
			
			
			rtn.add(added);
			notSrv.create(notifications.toArray(new Notification[]{}));
		}
		return rtn;
	}
	
	private void defineMediaType(Observation obs) {
		if (obs.getOnlinePath() != null && obs.getOnlinePath().trim().length() > 0) {
			File file = new File(obs.getOnlinePath());
			if (acceptedPictures(file)) {
				obs.setMediaType(picType);
			} else if (acceptedVideos(file)) {
				obs.setMediaType(vidType);
			} else if (acceptedAudios(file)) {
				obs.setMediaType(audType);
			} else { //If there's something but not a good format, send notification
				obs.setMediaType(notType); //TODO Define an ERROR type
				obs.setValidation(false);
				throw new IllegalArgumentException("Wrong file");
			}
		} else {
			obs.setMediaType(notType);
		}
	}
	
	private void definePublicLink(Observation obs) {
		if (!obs.getMediaType().equals(notType)) {
			obs.setPublicLink(storage.getPublicLink(Paths.get(obs.getOnlinePath())));
		}
	}
	
	private void checkBirdId(List<Notification> notifications, Observation obs) {
		if (obs.getBirdId() == 0) { //TODO Manage add new birds
			notifications.add(NotificationBuilder.getDefaultNotification(
				  "Oiseau inconnu",
				  "L'oiseau ajouté est inconnu", //TODO Give more informations
				  PENDING_STATUS,
				  obs));
		} else if (!brdSrv.exist(obs.getBirdId()))
			throw new IllegalArgumentException("Bird ID not correct");
	}
	
	/**
	 * Analyse the obs and DOESN'T update the observation
	 *
	 * @param obs The observation to analyse
	 * @throws Exception If something wrong happened
	 */
	private boolean analyseMedia(Observation obs, List<Notification> notifications) throws Exception {
		if (obs.getMediaType().equals(picType)) { //TODO Create thread not to block the process
			AnnotateImageResponse analyse = vision.simpleAnalyse(
				  Paths.get(obs.getOnlinePath()));
			obs.setAnalyseResult(analyse.getSafeSearchAnnotation().toString());
			checkNotification(obs, analyse, notifications);
			return true;
		}
		if (obs.getMediaType().equals(vidType) || obs.getMediaType().equals(audType)) { //TODO Add Google Cloud Video Intelligence
			obs.setAnalyseResult("ANALYSE NOT AVAILABLE\nAnalyse only available for : pictures");
		}
		return false;
	}
	
	public List<Observation> observationUpdater(Observation... observations) throws Exception {
		List<Observation> toUpdate = new ArrayList<>();
		List<Notification> notifications = new ArrayList<>();
		for (Observation obs : observations) {
			Observation oldObs = obsSrv.getById(obs.getId());
				
				/* CHECK THE PICTURE */
			String newPath = obs.getOnlinePath();
			String oldPath = oldObs.getOnlinePath();
			if (newPath != null && newPath.trim().length() > 0) { //That means there's something
				if (oldPath != null && oldPath.trim().length() > 0) {
					if (!oldPath.equals(newPath)) { //If not equals, there's something different
						defineMediaType(obs);
						analyseMedia(obs, notifications);
					}
				} else { //If old was null or empty, but not the new one, that means there's something new
					defineMediaType(obs);
					analyseMedia(obs, notifications);
				}
			} else if (oldPath != null && oldPath.trim().length() > 0) { //New doesn't have and old yes so delete old file
				storage.deleteMedia(Paths.get(oldPath));
			}
				
				/* CHECK THE BIRD */
			checkBirdId(notifications, obs);
			
			toUpdate.add(obsSrv.update(obs));
			notSrv.create(notifications.toArray(new Notification[]{}));
		}
		return toUpdate;
	}
	
	private void checkNotification(Observation obs, AnnotateImageResponse analyse, List<Notification> notifications) {
		
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
			notifications.add(NotificationBuilder.getDefaultNotification( //Send a notification
				  "Problème avec une observation",
				  message,
				  PENDING_STATUS,
				  obs
			));
			obs.setValidation(false);
			obsSrv.update(obs);
		} else if (!containsNotCaseSensitive(labels, "bird")) {
			
			StringBuilder message = new StringBuilder("Analyse de l'image : \n");
			StringBuilder toTranslate = new StringBuilder();
			for (EntityAnnotation entity : labels)
				toTranslate.append(entity.getDescription()).append(",");
			String[] translation = translate.simpleTranslateFromENToFR(toTranslate.toString()).split(",");
			
			for (int i = 0; i < translation.length; i++)
				message.append(
					  translation[i])
					  .append(" : ").append(labels.get(i).getScore()).append("\n");
			notifications.add(NotificationBuilder.getDefaultNotification( //Send a notification
				  "Aucun oiseau détecté",
				  message.toString(),
				  PENDING_STATUS,
				  obs
			));
			obs.setValidation(false);
			obsSrv.update(obs);
		}
	}
	
	private boolean containsNotCaseSensitive(List<EntityAnnotation> entities, String key) {
		boolean rtn = false;
		String lowerKey = key.toLowerCase();
		for (EntityAnnotation entity : entities)
			if (entity.getDescription().toLowerCase().contains(lowerKey)) {
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
