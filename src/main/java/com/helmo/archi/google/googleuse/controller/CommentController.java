package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Comment;
import com.helmo.archi.google.googleuse.service.CommentService;
import org.springframework.http.ResponseEntity;
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
		return cmtSrv.getById(id);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_USER")
	public List<Comment> create(@RequestBody Comment... comment) {
		return cmtSrv.create(comment);
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public List<Comment> update(Comment... comments) {
		return cmtSrv.update(comments);
	}
	
	@Override
	@DeleteMapping
	@Secured("ROLE_USER")
	public ResponseEntity delete(@RequestBody Comment... cmt) {
		cmtSrv.delete(cmt);
		return ResponseEntity.ok().build();
	}
	
	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity deleteOne(@PathVariable("id") long id) {
		cmtSrv.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
