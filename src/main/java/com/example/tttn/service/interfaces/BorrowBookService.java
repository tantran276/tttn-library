package com.example.tttn.service.interfaces;

import java.util.List;
import java.util.Map;

import com.example.tttn.entity.BorrowBook;

public interface BorrowBookService {

	void saveBorrowBook(BorrowBook borrowBook);

	List<BorrowBook> getAll();

	BorrowBook findById(long id);

	List<BorrowBook> findAllBorrowed();

	List<Map<String, Integer>> getCountForMonth(Long year, Long month);
	
}
