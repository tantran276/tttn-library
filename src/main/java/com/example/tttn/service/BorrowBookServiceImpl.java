package com.example.tttn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tttn.entity.BorrowBook;
import com.example.tttn.repository.BorrowBookRepository;
import com.example.tttn.service.interfaces.BorrowBookService;

@Service
public class BorrowBookServiceImpl implements BorrowBookService{

	@Autowired
	BorrowBookRepository borrowBookRepository;
	
	@Override
	public void saveBorrowBook(BorrowBook borrowBook) {
		borrowBookRepository.save(borrowBook);
	}

	@Override
	public List<BorrowBook> getAll() {
		return borrowBookRepository.findAll();
	}

	@Override
	public BorrowBook findById(long id) {
		return borrowBookRepository.getById(id);
	}

	@Override
	public List<BorrowBook> findAllBorrowed() {
		return borrowBookRepository.findAllBorrowed();
	}

}
