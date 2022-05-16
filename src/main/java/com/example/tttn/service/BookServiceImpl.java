package com.example.tttn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tttn.entity.Book;
import com.example.tttn.repository.BookRepository;
import com.example.tttn.service.interfaces.BookService;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	BookRepository bookRepository;
	
	@Override
	public List<Book> getAll() {
		return bookRepository.findAll();
	}

	@Override
	public Book findByTitle(String title) {
		return bookRepository.findByTitle(title);
	}

	@Override
	public Book findById(long id) {
		return bookRepository.getById(id);
	}

	@Override
	public void saveBook(Book book) {
		bookRepository.save(book);
	}

	@Override
	public void deleteById(long id) {
		bookRepository.delete(bookRepository.getById(id));
	}

	@Override
	public boolean existsByIsbn(String isbn) {
		return bookRepository.existsByIsbn(isbn);
	}

	@Override
	public List<Book> findBookByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	@Override
	public byte[] getImage(long id) {
		return bookRepository.getImageById(id);
	}

}
