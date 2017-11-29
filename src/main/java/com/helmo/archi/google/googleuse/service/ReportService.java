package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Report;
import com.helmo.archi.google.googleuse.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ReportService implements BasicService<Report, Long> {
	
	private final ReportRepository repRepo;
	
	public ReportService(ReportRepository repRepo) {
		this.repRepo = repRepo;
	}
	
	@Override
	public List<Report> getAll() {
		return repRepo.findAll();
	}
	
	@Override
	public Report getById(Long id) {
		return repRepo.findOne(id);
	}
	
	@Override
	public List<Report> create(Report... reports) {
		List<Report> rtn = new ArrayList<>();
		for (Report rpt : reports)
			rtn.add(create(rpt));
		return rtn;
	}
	
	@Override
	public Report create(Report rpt) {
		Report toSave = new Report();
		toSave.setCommentary(rpt.getCommentary());
		toSave.setDate(
				(rpt.getDate() != null)
						? rpt.getDate()
						: new Timestamp(new Date().getTime())
		);
		toSave.setObservation(rpt.getObservation());
		toSave.setUser(rpt.getUser());
		return repRepo.save(toSave);
	}
	
	@Override
	public Report update(Report updated) {
		Report rpt = repRepo.findOne(updated.getId());
		rpt.setCommentary(
				(updated.getCommentary() != null)
						? updated.getCommentary()
						: rpt.getCommentary()
		);
		return repRepo.save(rpt);
	}
	
	@Override
	public List<Report> update(Report... reports) {
		List<Report> rtn = new ArrayList<>();
		for(Report rpt : reports)
			rtn.add(create(rpt));
		return rtn;
	}
	
	@Override
	public void delete(Report rpt) {
		repRepo.delete(rpt.getId());
	}
	
	@Override
	public void delete(Report... reports) {
		repRepo.delete(Arrays.asList(reports));
	}
	
	@Override
	public void deleteById(Long id) {
		repRepo.delete(id);
	}
	
	public void deleteByObservation(Observation obs) {
		repRepo.deleteAllByObservation(obs);
	}
}
