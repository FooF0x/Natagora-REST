package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Report;
import com.helmo.archi.google.googleuse.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController implements BasicController<Report> { //TODO Get reports by user
	
	private final ReportService rptSrv;
	
	public ReportController(ReportService repSrv) {
		this.rptSrv = repSrv;
	}
	
	@Override
	@GetMapping
	@Secured("ROLE_ADMIN")
	public List<Report> getAll() {
		return rptSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	@Secured("ROLE_USER")
	public Report getOne(@PathVariable("id") long id) {
		return rptSrv.getById(id);
	}
	
	@GetMapping("/{one}/{two}")
	@Secured("ROLE_USER")
	public List<Report> getRange(@PathVariable("one") long one, @PathVariable("two") long two) {
		if (two <= one) throw new IllegalArgumentException("Wrong args");
		return rptSrv.getRange(one, two);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_USER")
	public ResponseEntity create(@RequestBody Report... reports) {
		try {
			return ResponseEntity.ok(rptSrv.create(reports));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public ResponseEntity update(@RequestBody Report... reports) {
		try {
			return ResponseEntity.ok(rptSrv.update(reports));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@DeleteMapping("/{id}")
	@Secured("ROLE_USER")
	public ResponseEntity deleteOne(@PathVariable("id") long id) {
		rptSrv.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	@Override
	@DeleteMapping
	@Secured("ROLE_USER")
	public ResponseEntity delete(@RequestBody Report... reports) {
		rptSrv.delete(reports);
		return ResponseEntity.ok().build();
	}
}
