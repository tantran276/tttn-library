package com.example.tttn.service.interfaces;

import java.util.List;

import com.example.tttn.entity.Book;

public interface BookService {
	List<Book> getAll();
	Book findByTitle(String title);
	Book findById(long id);
	void saveBook(Book book);
	void deleteById(long id);
	boolean existsByIsbn(String isbn);
	List<Book> findBookByIsbn(String isbn);
	byte[] getImage(long id);
}
