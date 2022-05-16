package com.example.tttn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tttn.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	@Query("SELECT d FROM Book d WHERE d.title = ?1")
	Book findByTitle(String title);
	
	boolean existsByIsbn(String isbn);
	
	List<Book> findByIsbn(String isbn);

	@Query("SELECT b.image FROM Book b WHERE b.id = ?1")
	byte[] getImageById(long id);
	
}
