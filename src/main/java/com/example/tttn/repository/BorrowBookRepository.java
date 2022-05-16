package com.example.tttn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tttn.entity.BorrowBook;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long>{

}
