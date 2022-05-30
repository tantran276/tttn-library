package com.example.tttn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	
	public Page<Book> findBooksWithPagination(int offset,int pageSize) {
		Page<Book> books = bookRepository.findAll(PageRequest.of(offset, pageSize));
		return books;
	}

	@Override
	public Book findByTitle(String title) {
		return bookRepository.findByTitle(title).get(0);
	}

	@Override
	public Book findById(long id) {
		return bookRepository.getById(id);
	}

	@Override
	public Book saveBook(Book book) {
		Book book1 = bookRepository.save(book);
		return book1;
	}

	@Override
	public void deleteById(long id) {
		bookRepository.deleteById(id);
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
	public List<Book> searchBook(String query) {
		return bookRepository.searchBooksByTitleOrContent(query);
	}

	@Override
	public List<Book> getAllByIsbn() {
		List<Book> books = new ArrayList<>();
		bookRepository.findAllByIsbn().forEach((isbn) -> {
			books.add(this.findByIsbn(isbn));
		});
		return books;
	}

	@Override
	public Book findByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn).get(0);
	}

	@Override
	public List<Book> searchByTitle(String title) {
		return bookRepository.searchBooksByTitle(title);
	}

	@Override
	public List<Book> searchByYear(int year) {
		return bookRepository.searchByYear(year);
	}

	@Override
	public long countBorrowedBook(String isbn) {
		return bookRepository.countBorrowBook(isbn);
	}

	@Override
	public long countReadyBook(String string) {
		return bookRepository.countReadyBook(string);
	}

	@Override
	public List<Book> findReadyBookByIsbn(String isbn) {
		return bookRepository.findReadyBookByIsbn(isbn);
	}

	@Override
	public byte[] getImage(String isbn) {
		return bookRepository.getImageByIsbn(isbn).get(0);
	}

	@Override
	public Map<String, Long> countBook() {
		Map<String, Long> countBook = new HashMap<>();
		countBook.put("Tổng số sách", bookRepository.countBook());
		countBook.put("Sách đã mượn", bookRepository.countBorrowBook());
		countBook.put("Sách đang rỗi", bookRepository.countReadyBook());
		return countBook;
	}

}
