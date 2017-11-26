package com.helmo.archi.google.googleuse.controller;

import com.google.cloud.vision.v1.Likelihood;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.helmo.archi.google.googleuse.ml.GoogleTranslate;
import com.helmo.archi.google.googleuse.ml.GoogleVision;
import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.service.NotificationService;
import com.helmo.archi.google.googleuse.service.ObservationService;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
	private final GoogleStorage storage;
	private final GoogleVision vision;
	private final GoogleTranslate translate;
	
	public ObservationController(ObservationService obsSrv, NotificationService notSrv,
	                             GoogleStorage storage, GoogleVision vision, GoogleTranslate translate) {
		this.obsSrv = obsSrv;
		this.notSrv = notSrv;
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
		//Analyse the pic
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
		obsSrv.deleteById(id);
	}
	
	@RequestMapping(value = "/downloadImage", method = RequestMethod.GET)
	public void downloadImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		int BUFFER_SIZE = 4096;
		String filePath = "C:\\Work\\IntelliJ\\google-use\\src\\main\\resources\\pic\\pig.jpg" ;
		// get the absolute path of the application
		ServletContext context = request.getSession().getServletContext();
		String appPath = context.getRealPath("");
		// construct the complete absolute path of the file
//		String fullPath = appPath + filePath;
		String fullPath = filePath;
		File downloadFile = new File(fullPath);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(fullPath);
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		// check the mime type
		System.out.println("MIME type: " + mimeType);
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);
		// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();
	}
}
