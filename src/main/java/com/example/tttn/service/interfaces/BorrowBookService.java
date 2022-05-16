package com.example.tttn.service.interfaces;

import java.util.List;

import com.example.tttn.entity.BorrowBook;

public interface BorrowBookService {

	void saveBorrowBook(BorrowBook borrowBook);

	List<BorrowBook> getAll();

	BorrowBook findById(long id);
	
}
