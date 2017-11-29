package com.helmo.archi.google.googleuse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

	@GetMapping
	public String index() { //TODO Define a Hello page
		return "Hello\n\tAuthors : GRIGNET Quentin - MARÉCHAL Thibaut";
	}
}
