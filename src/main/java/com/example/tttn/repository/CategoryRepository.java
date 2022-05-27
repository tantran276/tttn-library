package com.example.tttn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tttn.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	@Query("SELECT c FROM Category c WHERE c.categoryName = ?1")
	Category findByName(String name);
	
	boolean existsByCategoryName(String categoryName);

	@Query("SELECT c FROM Category c WHERE " +
			"c.id LIKE CONCAT('%', :query, '%')")
	List<Category> searchCategory(String query);
}
