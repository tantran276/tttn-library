package com.example.tttn.restcontroller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tttn.entity.BorrowBook;
import com.example.tttn.service.interfaces.BorrowBookService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/borrowbook")
public class BorrorwBookController {

	@Autowired
	BorrowBookService borrowBookService;
	
	@GetMapping()
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(borrowBookService.getAll());
	}
	
	@PutMapping("/return/{id}")
	public ResponseEntity<?> returnBorrowBook(@PathVariable long id) {
		BorrowBook borrowBook = borrowBookService.findById(id);
		borrowBook.setStatus(1);
		borrowBook.setReturnDate(new Date(System.currentTimeMillis()));
		borrowBook.getBook().setStatus(true);
		return ResponseEntity.ok("Success");
	}
	
	@PutMapping("/renewal/{id}")
	public ResponseEntity<?> renewalBorrowBook(@PathVariable long id, @RequestParam(name = "expirationDate") Date expirationDate) {
		BorrowBook borrowBook = borrowBookService.findById(id);
		borrowBook.setExpirationDate(expirationDate);
		return ResponseEntity.ok("Sucees");
	}
}
