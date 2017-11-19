package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Comment;
import com.helmo.archi.google.googleuse.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

	private final CommentService cmtSrv;
	
	public CommentController(CommentService cmtSrv) {
		this.cmtSrv = cmtSrv;
	}
	
	@GetMapping
	public List<Comment> getAll() {
		return cmtSrv.getAll();
	}
}
