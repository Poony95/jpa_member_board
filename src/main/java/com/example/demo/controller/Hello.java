package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class Hello {
	
	@GetMapping("/hello")
	public String hello(HttpServletRequest request) {
		String path = request.getServletContext().getRealPath("/images");
		System.out.println("path:"+path);
		return "OK";
	}
}






