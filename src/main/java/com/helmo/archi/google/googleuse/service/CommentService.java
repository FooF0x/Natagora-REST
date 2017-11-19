package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Comment;
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
}
