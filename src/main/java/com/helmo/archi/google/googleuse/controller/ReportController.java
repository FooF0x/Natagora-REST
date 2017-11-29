package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Report;
import com.helmo.archi.google.googleuse.service.ReportService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

	private final ReportService rptSrv;
	
	public ReportController(ReportService repSrv) {
		this.rptSrv = repSrv;
	}
	
	@GetMapping
	public List<Report> getAll() {
		return rptSrv.getAll();
	}
	
	@PostMapping
	@Secured("ROLE_USER")
	public Report createOne(@RequestBody Report rpt) {
		return rptSrv.create(rpt);
	}
	
	@PutMapping
	@Secured("ROLE_USER")
	public Report updateOne(@RequestBody Report rpt) {
		return rptSrv.update(rpt);
	}
	
	@DeleteMapping
	@Secured("ROLE_USER")
	public void deleteOne(@RequestBody Report rpt) {
		rptSrv.delete(rpt);
	}
}
