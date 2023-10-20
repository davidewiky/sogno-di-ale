package com.group.sogno.ale.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = { "/api" })
public class HelloWorldController {
	
	@GetMapping("/hello")
	public String test() {
		return "Hello World";
	}

}
