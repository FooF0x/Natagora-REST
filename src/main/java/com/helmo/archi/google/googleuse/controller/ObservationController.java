package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.service.CommentService;
import com.helmo.archi.google.googleuse.service.NotificationService;
import com.helmo.archi.google.googleuse.service.ObservationService;
import com.helmo.archi.google.googleuse.service.ReportService;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;
import com.helmo.archi.google.googleuse.tools.ObservationChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/observations")
public class ObservationController implements BasicController<Observation> {
	
	private final ObservationService obsSrv;
	private final NotificationService notSrv;
	private final ReportService repSrv;
	private final CommentService cmtSrv;
	private final GoogleStorage storage;
	private final ObservationChecker obsChecker;
	
	public ObservationController(ObservationService obsSrv, NotificationService notSrv, ReportService repSrv,
	                             CommentService cmtSrv, GoogleStorage storage, ObservationChecker obsChecker) {
		this.obsSrv = obsSrv;
		this.notSrv = notSrv;
		this.repSrv = repSrv;
		this.cmtSrv = cmtSrv;
		this.storage = storage;
		this.obsChecker = obsChecker;
	}
	
	@Override
	@GetMapping
	public List<Observation> getAll() {
		return obsSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	public Observation getOne(@PathVariable("id") long id) {
		return obsSrv.getById(id);
	}
	
	@Override
	@PostMapping
	public ResponseEntity create(@RequestBody Observation... observations) {
		try {
			return ResponseEntity.ok(obsChecker.observationAdder(null, observations));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@PutMapping
	public ResponseEntity update(@RequestBody Observation... observations) {
		try {
			return ResponseEntity.ok(obsChecker.observationUpdater(observations));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	public ResponseEntity delete(@RequestBody Observation... observations) {
		try {
			for (Observation obs : observations)
				deleteOne(obs.getId());
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@DeleteMapping("/{id}")
	@Secured("ROLE_USER")
	public ResponseEntity deleteOne(@PathVariable("id") long id) {
		try {
			Observation obs = obsSrv.getById(id);
			repSrv.deleteByObservation(obs); //Delete all reports
			cmtSrv.deleteByObservation(obs); //Delete all comments
			notSrv.deleteByObservation(obs); //Delete all notifications
			obsSrv.deleteById(id);
			storage.deleteMedia(Paths.get(obs.getOnlinePath()));
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}
	
}
