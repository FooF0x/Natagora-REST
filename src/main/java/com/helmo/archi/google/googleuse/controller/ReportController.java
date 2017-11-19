package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Report;
import com.helmo.archi.google.googleuse.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
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
	public void createOne(@RequestBody Report rpt) {
		rptSrv.create(rpt);
	}
}
