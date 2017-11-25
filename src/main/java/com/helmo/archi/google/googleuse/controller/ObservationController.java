package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Observation;
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
import java.util.List;

@RestController
@RequestMapping("/observations")
public class ObservationController {

	private final ObservationService obsSrv;
	private final GoogleStorage storage;
	
	public ObservationController(ObservationService obsSrv, GoogleStorage storage) {
		this.obsSrv = obsSrv;
		this.storage = storage;
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
		obsSrv.createObservation(obs);
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
