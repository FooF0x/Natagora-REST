package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Session;
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
	private final WeatherService wthSrv;
	private final ObservationChecker obsChecker;
	
	public SessionController(SessionService sesSrv, WeatherService wthSrv, ObservationChecker obsChecker) {
		this.sesSrv = sesSrv;
		this.wthSrv = wthSrv;
		this.obsChecker = obsChecker;
	}
	
	@Override
	@GetMapping()
	@Secured("ROLE_USER")
	public List<Session> getAll() {
		return sesSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	@Secured("ROLE_USER")
	public Session getOne(@PathVariable("id") long id) {
		return sesSrv.getById(id);
	}
	
	@GetMapping("/{one}/{two}")
	@Secured("ROLE_USER")
	public List<Session> getRange(@PathVariable("one") long one, @PathVariable("two") long two) {
		if (two <= one) throw new IllegalArgumentException("Wrong args");
		return sesSrv.getRange(one, two);
	}
	
	@GetMapping("/for/{id}")
	@Secured("ROLE_USER")
	public List<Session> getFor(@PathVariable("id") long idUser) {
		//TODO if a simple user is connected, check if it's its id
		return sesSrv.findByUserId(idUser);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_USER")
	public ResponseEntity create(@RequestBody Session... sessions) {
		try {
			List<Session> rtn = new ArrayList<>();
			for (Session ses : sessions) {
				/* GET THE WEATHER */
				String rawWeather = wthSrv.getWeather(ses.getLatitude(), ses.getLongitude());
				JsonParser springParser = JsonParserFactory.getJsonParser();
				Map<String, Object> result = springParser.parseMap(rawWeather);
				
				/* SET THE WEATHER */
				setWeatherData(ses, result);
				
				/* EXTRACT OBSERVATIONS */
				Observation[] obs = ses.getObservations().toArray(new Observation[ses.getObservations().size()]);
				ses.setObservations(new ArrayList<>());
				
				/* ADD SESSION */
				Session added = sesSrv.create(ses);
				
				/* ADD OBSERVATIONS*/
				added.setObservations(
					  obsChecker.observationAdder(added, obs));
				rtn.add(added);
			}
			return ResponseEntity.ok(rtn); //TODO Define transmission object with data and list of errors (Surround createObs with try/catch)
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	private void setWeatherData(Session ses, Map<String, Object> seed) {
		if (seed.containsKey("main")) {
			LinkedHashMap<String, Double> main = (LinkedHashMap<String, Double>) seed.get("main");
			ses.setTemperature(main.get("temp") - 273.15); //Convert from °K to °C
		} else ses.setTemperature(null);
		
		if (seed.containsKey("rain")) {
			LinkedHashMap<String, Double> rain = (LinkedHashMap<String, Double>) seed.get("rain");
			ses.setRain(rain.get("3h"));
		} else ses.setRain(null);
		
		if (seed.containsKey("wind")) {
			LinkedHashMap<String, Double> wind = (LinkedHashMap<String, Double>) seed.get("wind");
			ses.setWind(wind.get("speed"));
		} else ses.setWind(null);
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public ResponseEntity update(@RequestBody Session... ses) {
		try {
			return ResponseEntity.ok(sesSrv.update(ses));
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@Override
	public ResponseEntity deleteOne(long id) {
		sesSrv.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	@Override
	public ResponseEntity delete(Session... sessions) {
		sesSrv.delete(sessions);
		return ResponseEntity.ok().build();
	}
}
