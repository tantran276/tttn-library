package com.example.tttn.restcontroller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tttn.entity.BorrowBook;
import com.example.tttn.entity.User;
import com.example.tttn.mapper.BorrowBookResponseMapper;
import com.example.tttn.payload.response.BorrowBookResponse;
import com.example.tttn.service.interfaces.BorrowBookService;
import com.example.tttn.service.interfaces.ReservationService;
import com.example.tttn.service.interfaces.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/borrowbook")
public class BorrorwBookController {

	@Autowired
	BorrowBookService borrowBookService;

	@Autowired
	UserService userService;
	
	@Autowired
	ReservationService reservationService;
	
	@GetMapping()
	public ResponseEntity<?> getAllBorrowed(@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "pageSize", required = false) Integer pageSize) {
		if (offset == null) {
			offset = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		List<BorrowBookResponse> borrowBookResponses = new ArrayList<>();
		borrowBookService.findAllBorrowed().forEach((borrowBook) -> {
			if(borrowBook.getBorrowDate() != null && borrowBook.getStatus() == 1) {
				borrowBookResponses.add(BorrowBookResponseMapper.toBorrowBookResponse(borrowBook));
			}
		});
		return ResponseEntity.ok(this.paging(borrowBookResponses, offset, pageSize));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getBorrowedOfUser(@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "pageSize", required = false) Integer pageSize, @PathVariable(name = "id") long id) {
		if (offset == null) {
			offset = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		User user = userService.getUserById(id);
		List<BorrowBookResponse> borrowBookResponses = new ArrayList<>();
		user.getReservations().forEach((reservation) -> {
			if (reservation.getStatus() == 1 && reservation.getBorrowBook().getStatus() == 1) {
				borrowBookResponses.add(BorrowBookResponseMapper.toBorrowBookResponse(reservation.getBorrowBook()));
			}
		});
		return ResponseEntity.ok(this.paging(borrowBookResponses, offset, pageSize));
	}
	
	@GetMapping("/borrowing/{id}")
	public ResponseEntity<?> getBorrowingOfUser(@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "pageSize", required = false) Integer pageSize, @PathVariable(name = "id") long id) {
		if (offset == null) {
			offset = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		User user = userService.getUserById(id);
		List<BorrowBookResponse> borrowBookResponses = new ArrayList<>();
		user.getReservations().forEach((reservation) -> {
			if (reservation.getStatus() == 1 && reservation.getBorrowBook().getStatus() == 0) {
				borrowBookResponses.add(BorrowBookResponseMapper.toBorrowBookResponse(reservation.getBorrowBook()));
			}
		});
		return ResponseEntity.ok(this.paging(borrowBookResponses, offset, pageSize));
	}
	
	@GetMapping("/borrowing")
	public ResponseEntity<?> getBorrowingOfAll(@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "pageSize", required = false) Integer pageSize) {
		if (offset == null) {
			offset = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		List<BorrowBookResponse> borrowBookResponses = new ArrayList<>();
		borrowBookService.findAllBorrowed().forEach((borrowBook) -> {
			if(borrowBook.getBorrowDate() != null && borrowBook.getStatus() == 0) {
				borrowBookResponses.add(BorrowBookResponseMapper.toBorrowBookResponse(borrowBook));
			}
		});
		return ResponseEntity.ok(this.paging(borrowBookResponses, offset, pageSize));
	}
	
	@GetMapping("/countformonth")
	public ResponseEntity<?> getCountForMonth(@RequestParam(name = "year")Long year, @RequestParam(name = "month")Long month) {
		return ResponseEntity.ok(borrowBookService.getCountForMonth(year, month));
	}
	
	@PutMapping("/return/{id}")
	public ResponseEntity<?> returnBorrowBook(@PathVariable long id) {
		BorrowBook borrowBook = borrowBookService.findById(id);
		borrowBook.setStatus(1);
		borrowBook.setReturnDate(new Date(System.currentTimeMillis()));
		borrowBook.getBook().setStatus(true);
		borrowBookService.saveBorrowBook(borrowBook);
		return ResponseEntity.ok("Success");
	}
	
	

	@PutMapping("/renewal/{id}")
	public ResponseEntity<?> renewalBorrowBook(@PathVariable long id) {
		BorrowBook borrowBook = borrowBookService.findById(id);
		borrowBook.setExpirationDate(this.addDayToJavaUtilDate(borrowBook.getExpirationDate(), 30));
		borrowBookService.saveBorrowBook(borrowBook);
		return ResponseEntity.ok("Sucees");
	}

	public Page<?> paging(List<?> list, int offset, int pageSize) {
		Pageable pageable = PageRequest.of(offset, pageSize);
		if (list.size() <= (offset * pageSize)) {
			return new PageImpl<>(new ArrayList<>(), PageRequest.of(offset, pageSize), list.size());
		}
		final int toIndex = Math.min((pageable.getPageNumber() + 1) * pageable.getPageSize(), list.size());
		final int fromIndex = offset*pageSize;
		System.out.println(list.size());
		return new PageImpl<>(list.subList(fromIndex, toIndex), pageable, list.size());
	}
	
	public Date addDayToJavaUtilDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}
}
