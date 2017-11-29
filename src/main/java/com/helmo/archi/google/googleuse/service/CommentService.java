package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Comment;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommentService implements BasicService<Comment, Long> {

	private final CommentRepository cmtRepo;
	
	public CommentService(CommentRepository cmtRepo) {
		this.cmtRepo = cmtRepo;
	}
	
	@Override
	public List<Comment> getAll() {
		return cmtRepo.findAll();
	}
	
	@Override
	public Comment getById(Long id) {
		return cmtRepo.findOne(id);
	}
	
	@Override
	public Comment create(Comment comment) {
		return cmtRepo.save(comment);
	}
	
	@Override
	public List<Comment> create(Comment... comments) {
		return cmtRepo.save(Arrays.asList(comments));
	}
	
	@Override
	public Comment update(Comment updated) {
		Comment cmt = cmtRepo.findOne(updated.getId());
		cmt.setCommentary(updated.getCommentary());
		return cmtRepo.save(cmt);
	}
	
	@Override
	public List<Comment> update(Comment... comments) {
		List<Comment> rtn = new ArrayList<>();
		for(Comment cmt : comments)
			rtn.add(update(cmt));
		return rtn;
	}
	
	@Override
	public void delete(Comment cmt) {
		cmtRepo.delete(cmt.getId());
		cmt.deleteId();
		return cmt;
	}
	
	@Override
	public void delete(Comment... comments) {
		return null;
	}
	
	@Override
	public void deleteById(Long id) {
		cmtRepo.delete(id);
	}
	
	public void deleteByObservation(Observation obs) {
		cmtRepo.deleteAllByObservation(obs);
	}
}
