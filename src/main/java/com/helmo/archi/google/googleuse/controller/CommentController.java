package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Comment;
import com.helmo.archi.google.googleuse.service.CommentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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
	
	@PostMapping
	@Secured("ROLE_USER")
	public Comment saveOne(@RequestBody Comment comment) {
		return cmtSrv.saveOne(comment);
	}
	
	@PutMapping
	@Secured("ROLE_USER")
	public Comment updateOne(@RequestBody Comment cmt) {
		return cmtSrv.updateOne(cmt);
	}
	
	@DeleteMapping
	@Secured("ROLE_USER")
	public void deleteOne(@RequestBody Comment cmt) {
		cmtSrv.deleteOne(cmt);
	}
	
}
