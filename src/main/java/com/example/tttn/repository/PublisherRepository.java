package com.example.tttn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tttn.entity.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long>{
	@Query("SELECT a FROM Publisher a WHERE a.name = ?1")
	Publisher findByName(String name);
	
	boolean existsByName(String name);
	
}