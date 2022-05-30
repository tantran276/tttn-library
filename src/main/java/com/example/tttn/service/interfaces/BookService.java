package com.example.tttn.service.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.example.tttn.entity.Book;

public interface BookService {
	List<Book> getAll();
	Book findByTitle(String title);
	Book findByIsbn(String isbn);
	Book findById(long id);
	Book saveBook(Book book);
	void deleteById(long id);
	boolean existsByIsbn(String isbn);
	List<Book> findBookByIsbn(String isbn);
	byte[] getImage(String isbn);
	List<Book> searchBook(String query);
	Page<Book> findBooksWithPagination(int offset,int pageSize);
	List<Book> getAllByIsbn();
	List<Book> searchByTitle(String title);
	List<Book> searchByYear(int year);
	long countBorrowedBook(String string);
	long countReadyBook(String string);
	List<Book> findReadyBookByIsbn(String isbn);
	Map<String, Long> countBook();
}
