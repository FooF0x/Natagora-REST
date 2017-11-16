package com.helmo.archi.google.googleuse.tools;

import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
@PropertySource("classpath:application.properties")
public class HELMoCredentialsProvider {
	public static ServiceAccountCredentials getCredential() {
		try {
			return ServiceAccountCredentials.fromStream(
					new FileInputStream(
							new ClassPathResource("service_key.json").getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; //TODO To change
	}
}
