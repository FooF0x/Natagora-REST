package com.helmo.archi.google.googleuse.ml;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.helmo.archi.google.googleuse.tools.HELMoCredentialsProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleVision {
	
	ImageAnnotatorSettings settings;
	
	public GoogleVision() throws IOException {
		CredentialsProvider credentialsProvider = FixedCredentialsProvider
				.create(HELMoCredentialsProvider.getCredential());
		settings = ImageAnnotatorSettings
				.newBuilder()
				.setCredentialsProvider(credentialsProvider)
				.build();
	}
	
	private BatchAnnotateImagesResponse performDetection(String localPath, Type type) throws Exception {
		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(settings)) {
			// Reads the image file into memory
			Path path = Paths.get(localPath);
			byte[] data = Files.readAllBytes(path);
			ByteString imgBytes = ByteString.copyFrom(data);
			
			// Builds the image annotation request
			List<AnnotateImageRequest> requests = new ArrayList<>();
			Image img = Image.newBuilder()
					.setContent(imgBytes)
					.build();
			Feature feat = Feature.newBuilder()
					.setType(type)
					.build();
			AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
					.addFeatures(feat)
					.setImage(img)
					.build();
			requests.add(request);
			
			// Performs the detection
			return vision.batchAnnotateImages(requests);
		}
	}
	
	public List<String> getRGB(String localPath) throws Exception {
		List<String> rgb = new ArrayList<>();
		for (AnnotateImageResponse res : performDetection(localPath, Type.IMAGE_PROPERTIES).getResponsesList()) {
			if (res.hasError()) {
				break;
			}
			
			DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
			for (ColorInfo color : colors.getColorsList()) {
				rgb.add(String.format("%.0f;%.0f;%.0f",
						color.getColor().getRed(),
						color.getColor().getGreen(),
						color.getColor().getBlue()));
			}
		}
		return rgb;
	}
	
	public Map<String, String> labelAsMap(String localPath) throws Exception {
		Map<String, String> labels = new HashMap<>();
		String temp = "";
		for (AnnotateImageResponse res : performDetection(localPath, Type.LABEL_DETECTION).getResponsesList()) {
			if (res.hasError()) {
				break;
			}
			
			for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
				for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : annotation.getAllFields().entrySet()) {
					if(entry.getKey().toString().contains("description")) temp = entry.getValue().toString();
					else if (entry.getKey().toString().contains("score")) {
						labels.put(temp, entry.getValue().toString());
					}
				}
			}
		}
		
		return labels;
	}
	
	public String safeSearchAnalyze(String localPath) throws Exception {
		return performDetection(localPath, Type.SAFE_SEARCH_DETECTION).toString();
	}
}
