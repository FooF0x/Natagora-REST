package com.helmo.archi.google.googleuse.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
	
	private final String KEY = "4f953e4c0ac499d47466ae6bd0b08c3b";
	private final String URL;
	private final RestTemplate restTemplate;
	
	public WeatherService(RestTemplateBuilder restTemplateBuilder, Environment env) {
		this.restTemplate = restTemplateBuilder.build();
		URL = env.getProperty("weather.uri");
	}
	
	public String getWeather(String lat, String lon) {
		return this.restTemplate.getForObject(
			  URL, String.class, lat, lon, KEY);
	}
}
