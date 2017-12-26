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
	
	@GetMapping("/{one}/{two}")
	@Secured("ROLE_USER")
	public List<Comment> getRange(@PathVariable("one") long one, @PathVariable("two") long two) {
		if (two <= one) throw new IllegalArgumentException("Wrong args");
		return cmtSrv.getRange(one, two);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_USER")
	public ResponseEntity create(@RequestBody Comment... comments) {
		try {
			return ResponseEntity.ok(cmtSrv.create(comments));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public ResponseEntity update(Comment... comments) {
		try {
			return ResponseEntity.ok(cmtSrv.update(comments));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
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
