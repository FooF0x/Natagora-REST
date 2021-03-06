package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.model.NotificationStatus;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.service.*;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;
import com.helmo.archi.google.googleuse.tools.ObservationChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/observations")
public class ObservationController implements BasicController<Observation> {
	
	private final ObservationService obsSrv;
	private final NotificationService notSrv;
	private final NotificationStatusService notStatSrv;
	private final ReportService repSrv;
	private final CommentService cmtSrv;
	private final GoogleStorage storage;
	private final ObservationChecker obsChecker;
	
	public ObservationController(ObservationService obsSrv, NotificationService notSrv, NotificationStatusService notStatSrv,
	                             ReportService repSrv,
	                             CommentService cmtSrv, GoogleStorage storage, ObservationChecker obsChecker) {
		this.obsSrv = obsSrv;
		this.notSrv = notSrv;
		this.notStatSrv = notStatSrv;
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
	
	@GetMapping("/{one}/{two}")
	@Secured("ROLE_USER")
	public List<Observation> getRange(@PathVariable("one") long one, @PathVariable("two") long two) {
		return obsSrv.getRange(one, two);
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
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@Override
	@DeleteMapping
	public ResponseEntity delete(@RequestBody Observation... observations) {
		try {
			Arrays.asList(observations).forEach(
				  this::delete
			);
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@Override
	@DeleteMapping("/{id}")
	@Secured("ROLE_USER")
	public ResponseEntity deleteOne(@PathVariable("id") Long id) {
		try {
			Observation obs = obsSrv.getById(id);
			delete(obs);
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}
	
	private void delete(Observation obs) {
		obsSrv.delete(obs);
		storage.deleteMedia(Paths.get(obs.getOnlinePath()));
	}
	
	@PutMapping("/validate/{id}")
	public void validate(@PathVariable("id") long id) {
		NotificationStatus accepted = notStatSrv.findByName("ACCEPTED");
		Observation obs = obsSrv.getById(id);
		updateNotificationsFor(id, accepted);
		obs.setValidation(true);
		obsSrv.update(obs);
	}
	
	@PutMapping("/refused/{id}")
	public void refused(@PathVariable("id") long id) {
		NotificationStatus refused = notStatSrv.findByName("REFUSED");
		Observation obs = obsSrv.getById(id);
		updateNotificationsFor(id, refused);
		obs.setValidation(false);
		obsSrv.update(obs);
	}
	
	private void updateNotificationsFor(Long id, NotificationStatus status) {
		List<Notification> notifications = notSrv.getByObservationId(id);
		notifications.forEach(
			  n -> n.setStatus(status)
		);
		notSrv.update(notifications.toArray(new Notification[]{}));
	}
	
}
