package com.example.tttn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tttn.entity.Author;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{
	@Query("SELECT a FROM Author a WHERE a.name = ?1")
	Author findByName(String name);
	
	boolean existsByName(String name);

	@Query("SELECT a FROM Author a WHERE " +
			"a.name LIKE CONCAT('%', :query, '%')")
	List<Author> searchAuthor(String query);
}
