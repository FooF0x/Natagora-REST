package com.helmo.archi.google.googleuse.ml;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.helmo.archi.google.googleuse.tools.HELMoCredentialsProvider;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GoogleVision {
	
	private final Environment env;
	private ImageAnnotatorSettings settings;
	
	public GoogleVision(Environment env) throws IOException {
		this.env = env;
		CredentialsProvider credentialsProvider = FixedCredentialsProvider
			  .create(HELMoCredentialsProvider.getCredential());
		settings = ImageAnnotatorSettings
			  .newBuilder()
			  .setCredentialsProvider(credentialsProvider)
			  .build();
	}
	
	private BatchAnnotateImagesResponse performDetectionOnline(String onlinePath, Type type) throws Exception {
		List<AnnotateImageRequest> requests = new ArrayList<>();
		
		ImageSource imgSource = ImageSource.newBuilder()
			  .setGcsImageUri(onlinePath)
			  .build();
		Image img = Image.newBuilder()
			  .setSource(imgSource)
			  .build();
		Feature feat = Feature.newBuilder()
			  .setType(type)
			  .build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
			  .addFeatures(feat)
			  .setImage(img)
			  .build();
		requests.add(request);
		
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create(settings)) {
			return client.batchAnnotateImages(requests);
		}
	}
	
	private BatchAnnotateImagesResponse performTwoDetectionOnline(String onlinePath, Type typeOne, Type typeTwo) throws Exception {
		List<AnnotateImageRequest> requests = new ArrayList<>();
		
		ImageSource imgSource = ImageSource.newBuilder()
			  .setGcsImageUri(onlinePath)
			  .build();
		Image img = Image.newBuilder()
			  .setSource(imgSource)
			  .build();
		Feature featOne = Feature.newBuilder()
			  .setType(typeOne)
			  .build();
		Feature featTwo = Feature.newBuilder()
			  .setType(typeTwo)
			  .build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
			  .addFeatures(featOne).addFeatures(featTwo)
			  .setImage(img)
			  .build();
		requests.add(request);
		
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create(settings)) {
			return client.batchAnnotateImages(requests);
		}
	}
	
	public Map<String, String> labelAsMap(String localPath) throws Exception {
		Map<String, String> labels = new HashMap<>();
		String temp = "";
//		for (AnnotateImageResponse res : performDetectionLocal(localPath, Type.LABEL_DETECTION).getResponsesList()) {
//			if (res.hasError()) {
//				break;
//			}
//
//			for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
//				for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : annotation.getAllFields().entrySet()) {
//					if (entry.getKey().toString().contains("description")) temp = entry.getValue().toString();
//					else if (entry.getKey().toString().contains("score")) {
//						labels.put(temp, entry.getValue().toString());
//					}
//				}
//			}
//		}
		
		return labels;
	}
	
	public SafeSearchAnnotation safeSearchAnalyse(Path onlinePath) throws Exception {
		String path;
		if (onlinePath.startsWith("/"))
			path = "gs://" + env.getProperty("storage.bucketName") + onlinePath;
		else
			path = "gs://" + env.getProperty("storage.bucketName") + "/" + onlinePath;
		
		performDetectionOnline(path, Type.SAFE_SEARCH_DETECTION)
			  .getResponses(0);
		
		return performDetectionOnline(path, Type.SAFE_SEARCH_DETECTION)
			  .getResponses(0)
			  .getSafeSearchAnnotation();
	}
	
	public AnnotateImageResponse simpleAnalyse(Path onlinePath) throws Exception {
		String path;
		if (onlinePath.startsWith("/"))
			path = "gs://" + env.getProperty("storage.bucketName") + onlinePath.toString().replace("\\", "/");
		else
			path = "gs://" + env.getProperty("storage.bucketName") + "/" + onlinePath.toString().replace("\\", "/");
		
		return performTwoDetectionOnline(path, Type.SAFE_SEARCH_DETECTION, Type.LABEL_DETECTION)
			  .getResponses(0);
	}
}
