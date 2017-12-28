package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Report;
import com.helmo.archi.google.googleuse.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ReportService implements AccessRange<Report, Long>{
	
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
	
	public List<Report> getByUserId(Long id) {
		return repRepo.getByUser_Id(id);
	}
	
	public List<Report> getByObservationId(Long id) {
		return repRepo.getByObservation_Id(id);
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
		rpt.setDate(
			  (updated.getDate() != null)
					? updated.getDate()
					: rpt.getDate()
		);
		rpt.setUser(
			  (updated.getUser() != null)
					? updated.getUser()
					: rpt.getUser()
		);
		rpt.setObservation(
			  (updated.getObservation() != null)
					? updated.getObservation()
					: rpt.getObservation()
		);
		return repRepo.save(rpt);
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
