package com.example.tttn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tttn.entity.BorrowBook;
import com.example.tttn.repository.BorrowBookRepository;
import com.example.tttn.service.interfaces.BorrowBookService;

@Service
public class BorrowBookServiceImpl implements BorrowBookService{

	@Autowired
	BorrowBookRepository borrowBookRepository;
	
	@Override
	public void saveBorrowBook(BorrowBook borrowBook) {
		borrowBookRepository.save(borrowBook);
	}

	@Override
	public List<BorrowBook> getAll() {
		return borrowBookRepository.findAll();
	}

	@Override
	public BorrowBook findById(long id) {
		return borrowBookRepository.getById(id);
	}

	@Override
	public List<BorrowBook> findAllBorrowed() {
		return borrowBookRepository.findAllBorrowed();
	}

	@Override
	public List<Map<String, Integer>> getCountForMonth(Long year, Long month) {
		List<Map<String, Integer>> countForMonth = new ArrayList<>();
		Map<String, Integer> countForDay = new HashedMap();
		List<Integer> dayBorrows = borrowBookRepository.findDayBorrowInMonth(year, month);
		List<Integer> dayReturns = borrowBookRepository.findDayReturnInMonth(year, month);
		List<Integer> borrowCounts = borrowBookRepository.findBorrowInMonth(year, month);
		List<Integer> returnCounts = borrowBookRepository.findReturnInMonth(year, month);
		for(int i = 1; i <= 31; i++) {
			if(dayBorrows.contains(i) || dayReturns.contains(i)) {	
				countForDay = new HashedMap();
				System.out.println("day: " + i);
				countForDay.put("day", i);
				if(dayBorrows.contains(i)) {
					countForDay.put("borrow", borrowCounts.get(dayBorrows.indexOf(i)));
				} else {
					countForDay.put("borrow", 0);
				}
				if(dayReturns.contains(i)) {
					countForDay.put("return", returnCounts.get(dayReturns.indexOf(i)));
				} else {
					countForDay.put("return", 0);
				}
				countForMonth.add(countForDay);			
			}
			
		}
		return countForMonth;
	}
	
	

}
