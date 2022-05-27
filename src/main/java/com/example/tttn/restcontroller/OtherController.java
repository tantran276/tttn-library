package com.example.tttn.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tttn.repository.CategoryRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class OtherController {

	@Autowired
	CategoryRepository categoryRepository;
	
	@GetMapping("/category")
	public ResponseEntity<?> getAllCategory() {
		return ResponseEntity.ok(categoryRepository.findAll());
	}
}
