package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Comment;
import com.helmo.archi.google.googleuse.service.CommentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController implements BasicController<Comment> {

	private final CommentService cmtSrv;
	
	public CommentController(CommentService cmtSrv) {
		this.cmtSrv = cmtSrv;
	}
	
	@Override
	@GetMapping
	public List<Comment> getAll() {
		return cmtSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	public Comment getOne(@PathVariable("id") long id) {
		return cmtSrv.getOne(id);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_USER")
	public List<Comment> create(@RequestBody Comment... comment) {
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
