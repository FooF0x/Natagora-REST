package com.helmo.archi.google.googleuse.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
	
	private final String KEY = "4f953e4c0ac499d47466ae6bd0b08c3b";
	private final RestTemplate restTemplate;
	
	public WeatherService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	
	public String getWeather(String lat, String lon) {
		return this.restTemplate.getForObject(
			  "http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={key}",
			  String.class, lat, lon, KEY);
	}
}
