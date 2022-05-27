package com.example.tttn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tttn.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	@Query("SELECT d FROM Book d WHERE d.title = ?1")
	List<Book> findByTitle(String title);
	
	boolean existsByIsbn(String isbn);
	
	List<Book> findByIsbn(String isbn);

	@Query("SELECT b.image FROM Book b WHERE b.isbn = ?1")
	List<byte[]> getImageByIsbn(String isbn);
	
	@Query("SELECT b FROM Book b WHERE " +
			"b.title LIKE CONCAT('%', :query, '%')" + 
			"Or b.content LIKE CONCAT('%', :query, '%')")
	List<Book> searchBooksByTitleOrContent(String query);
	
	@Query("SELECT b FROM Book b WHERE " +
			"b.title LIKE CONCAT('%', :query, '%')")
	List<Book> searchBooksByTitle(String query);

	@Query("select DISTINCT(b.isbn) from Book b")
	List<String> findAllByIsbn();
	
	@Query("SELECT b FROM Book b WHERE YEAR(b.createDate) = ?1")
	List<Book> searchByYear(int year);

	@Query("SELECT COUNT(b) FROM Book b WHERE b.isbn = ?1"+ 
			" AND b.status = false")
	long countBorrowBook(String isbn);
	
	@Query("SELECT COUNT(b) FROM Book b WHERE b.isbn = ?1"+ 
			" AND b.status = true")
	long countReadyBook(String isbn);

	@Query("SELECT b FROM Book b WHERE b.isbn = ?1"+ 
			" AND b.status = true")
	List<Book> findReadyBookByIsbn(String isbn);
}
