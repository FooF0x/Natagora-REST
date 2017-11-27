package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Comment;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

	private final CommentRepository cmtRepo;
	
	public CommentService(CommentRepository cmtRepo) {
		this.cmtRepo = cmtRepo;
	}
	
	public List<Comment> getAll() {
		return cmtRepo.findAll();
	}
	
	public Comment saveOne(Comment comment) {
		return cmtRepo.save(comment);
	}
	
	public Comment updateOne(Comment updated) {
		Comment cmt = cmtRepo.findOne(updated.getId());
		cmt.setCommentary(updated.getCommentary());
		return cmtRepo.save(cmt);
	}
	
	public void deleteOne(Comment cmt) {
		cmtRepo.delete(cmt.getId());
	}
	
	public void deleteByObservation(Observation obs) {
		cmtRepo.deleteAllByObservation(obs);
	}
}
