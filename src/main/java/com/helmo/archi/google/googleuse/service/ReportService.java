package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Report;
import com.helmo.archi.google.googleuse.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

	private final ReportRepository repRepo;
	
	public ReportService(ReportRepository repRepo) {
		this.repRepo = repRepo;
	}
	
	public List<Report> getAll() {
		return repRepo.findAll();
	}
	
	public List<Report> create(Iterable<Report> rpt) {
		return repRepo.save(rpt);
	}
	
	public Report create(Report rpt) {
		return repRepo.save(rpt);
	}
}
