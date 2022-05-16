package com.example.tttn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tttn.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{

	@Query("SELECT t FROM Tag t WHERE t.name = ?1")
	Tag findByName(String name);
	
	boolean existsByName(String name);
}
