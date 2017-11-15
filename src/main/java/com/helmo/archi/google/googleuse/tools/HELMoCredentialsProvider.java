package com.helmo.archi.google.googleuse.tools;

import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;

public class HELMoCredentialsProvider {
	public static ServiceAccountCredentials getCredential() throws IOException {
		return ServiceAccountCredentials.fromStream(new FileInputStream("rsc\\service_key.json"));
	}
}
