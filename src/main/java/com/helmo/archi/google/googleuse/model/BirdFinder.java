package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BirdFinder {
	
	//First Input
	private Map<String, String> single;
	private Map<String, List<String>> multiple;
	
	//Process Input
	private Map<String, Long> longItems;
	private Map<String, Double> doubleItems;
	
	public void processInput() {
		for (String key : single.keySet()) {
			String item = single.get(key);
			if (isNumeric(item)) {
				double temp = Double.parseDouble(item);
				try {
					if (temp == (int) temp)
						longItems.put(key, Long.parseLong(item));
				} catch (Exception e) {
					doubleItems.put(key, temp);
				} finally {
					single.remove(key);
				}
			}
		}
	}
	
	private boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}
	
}
