package com.example.tttn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tttn.entity.BorrowBook;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long>{

	@Query("Select b from BorrowBook b where not b.status = -2 ")
	List<BorrowBook> findAllBorrowed();

	@Query(value = "Select day(b.borrow_date) from borrow_book b where YEAR(b.borrow_date) = :year and MONTH(b.borrow_date)= :month group by day(b.borrow_date)", nativeQuery = true)
	List<Integer> findDayBorrowInMonth(Long year, Long month);

	@Query(value = "Select day(b.return_date) from borrow_book b where YEAR(b.return_date) = :year and MONTH(b.return_date)= :month group by day(b.return_date)", nativeQuery = true)
	List<Integer> findDayReturnInMonth(Long year, Long month);

	@Query(value = "Select count(b.id) from borrow_book b where YEAR(b.borrow_date) = :year and MONTH(b.borrow_date)= :month group by day(b.borrow_date)", nativeQuery = true)
	List<Integer> findBorrowInMonth(Long year, Long month);

	@Query(value = "Select count(b.id) from borrow_book b where YEAR(b.return_date) = :year and MONTH(b.return_date)= :month group by day(b.return_date)", nativeQuery = true)
	List<Integer> findReturnInMonth(Long year, Long month);
}
