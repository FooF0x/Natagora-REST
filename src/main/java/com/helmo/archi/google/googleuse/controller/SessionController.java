package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.service.BirdService;
import com.helmo.archi.google.googleuse.service.SessionService;
import com.helmo.archi.google.googleuse.service.WeatherService;
import com.helmo.archi.google.googleuse.tools.ObservationChecker;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sessions")
public class SessionController implements BasicController<Session> {
	
	private final SessionService sesSrv;
	private final BirdService brdSrv;
	private final WeatherService wthSrv;
	private final ObservationChecker obsChecker;
	
	public SessionController(SessionService sesSrv, BirdService brdSrv,
	                         WeatherService wthSrv, ObservationChecker obsChecker) {
		this.sesSrv = sesSrv;
		this.brdSrv = brdSrv;
		this.wthSrv = wthSrv;
		this.obsChecker = obsChecker;
	}
	
	@Override
	@GetMapping()
	@Secured("ROLE_USER")
	public List<Session> getAll() {
		List<Session> sessions = sesSrv.getAll();
		for(Session ses : sessions) {
			for(Observation obs : ses.getObservations()) {
				obs.setBird(brdSrv.getById(obs.getBirdId()));
			}
		}
		return sessions;
	}
	
	@Override
	@GetMapping("/{id}")
	@Secured("ROLE_USER")
	public Session getOne(@PathVariable("id") long id) {
		return sesSrv.getById(id);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_USER")
	public ResponseEntity create(@RequestBody Session... sessions) {
		/* TODO Define weather*/
		
		try {
			List<Session> rtn = new ArrayList<>();
			for (Session ses : sessions) {
				/* GET THE WEATHER */
				String rawWeather = wthSrv.getWeather(ses.getLatitude(), ses.getLongitude());
				JsonParser springParser = JsonParserFactory.getJsonParser();
				Map<String, Object> result = springParser.parseMap(rawWeather);
				
				if(result.containsKey("main")) {
					LinkedHashMap<String, Double> main = (LinkedHashMap<String, Double>) result.get("main");
					if(main.containsValue("temp"))
						ses.setTemperature(main.get("temp"));
					
				}
				if(result.containsKey("rain")) {
					LinkedHashMap<String, Double> rain = (LinkedHashMap<String, Double>) result.get("rain");
					if(rain.containsValue("3h"))
						ses.setTemperature(rain.get("3h"));
				} else {
					ses.setRain(0.0);
				}
				if(result.containsKey("wind")) {
					LinkedHashMap<String, Double> wind = (LinkedHashMap<String, Double>) result.get("wind");
					if(wind.containsValue("speed"))
						ses.setTemperature(wind.get("speed"));
				} else {
					ses.setWind(0.0);
				}
				
				
				Observation[] obs = ses.getObservations().toArray(new Observation[ses.getObservations().size()]);
				ses.setObservations(new ArrayList<>());
				Session added = sesSrv.create(ses);
				added.setObservations(
					  obsChecker.observationAdder(added, obs));
				rtn.add(added);
			}
			return ResponseEntity.ok(rtn);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public ResponseEntity update(@RequestBody Session... ses) {
		try {
			return ResponseEntity.ok(sesSrv.update(ses));
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	public ResponseEntity deleteOne(long id) {
		sesSrv.deleteById(id);
		return ResponseEntity.ok(null);
	}
	
	@Override
	public ResponseEntity delete(Session... sessions) {
		sesSrv.delete(sessions);
		return ResponseEntity.ok(null);
	}
}
