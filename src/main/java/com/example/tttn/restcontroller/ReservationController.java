package com.example.tttn.restcontroller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tttn.entity.Book;
import com.example.tttn.entity.BorrowBook;
import com.example.tttn.entity.Reservation;
import com.example.tttn.mapper.ReservationResponseMapper;
import com.example.tttn.payload.request.ReservationRequest;
import com.example.tttn.payload.response.ReservationResponse;
import com.example.tttn.service.interfaces.BookService;
import com.example.tttn.service.interfaces.BorrowBookService;
import com.example.tttn.service.interfaces.ReservationService;
import com.example.tttn.service.interfaces.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

	@Autowired
	ReservationService reservationService;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	BorrowBookService borrowBookService;
	
	@GetMapping()
	public ResponseEntity<?> getAllReservation() {
		List<ReservationResponse> reservations = new ArrayList<ReservationResponse>();
		reservationService.getAll().forEach((reservation) -> {
			reservations.add(ReservationResponseMapper.toReservationResponse(reservation));
		});
		return ResponseEntity.ok(reservations);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getAllReservation(@PathVariable long id) {
		ReservationResponse reservation = ReservationResponseMapper.toReservationResponse(reservationService.findReservationById(id));
		return ResponseEntity.ok(reservation);
	}
	
	@PostMapping()
	public ResponseEntity<?> reservationBook(@RequestBody ReservationRequest request) {
		Book book = bookService.findById(request.getBookId());
		if(!book.isStatus()) {
			return ResponseEntity.badRequest().body("Sach da duoc muon");
		}
		book.setStatus(false);
		BorrowBook borrowBook = new BorrowBook();
		borrowBook.setBook(bookService.findById(request.getBookId()));
		borrowBook.setBorrowDate(new Date(System.currentTimeMillis()));
		borrowBook.setExpirationDate(addDayToJavaUtilDate(borrowBook.getBorrowDate(), 3));
		borrowBook.setStatus(-2);
		borrowBookService.saveBorrowBook(borrowBook);
		Reservation reservation = new Reservation();
		reservation.setUser(userService.getUserById(request.getUserId()));
		reservation.setBorrowBook(borrowBook);
		reservation.setReservationDate(new Date(System.currentTimeMillis()));
		reservation.setExpirationDate(addDayToJavaUtilDate(reservation.getReservationDate(), 3));
		reservation.setStatus(0);
		reservationService.saveReservation(reservation);
		return ResponseEntity.ok("Success");
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteReservation(@PathVariable long id) {
		reservationService.deleteReservation(id);
		return ResponseEntity.ok("Success");
	}
	
	@PutMapping("/cancel/{id}")
	public ResponseEntity<?> cancelReservation(@PathVariable long id) {
		Reservation reservation = reservationService.findReservationById(id);
		reservation.setStatus(-1);
		reservation.getBorrowBook().setStatus(1);
		reservation.getBorrowBook().getBook().setStatus(true);
		reservationService.saveReservation(reservation);
		return ResponseEntity.ok("Success");
	}
	
	@PutMapping("/renewal/{id}")
	public ResponseEntity<?> renewalReservation(@PathVariable long id, @RequestParam(name = "expirationDate") Date expirationDate) {
		Reservation reservation = reservationService.findReservationById(id);
		reservation.setExpirationDate(expirationDate);
		reservation.getBorrowBook().setExpirationDate(expirationDate);
		return ResponseEntity.ok("Success");
	}
	
	@PutMapping("/accept")
	public ResponseEntity<?> acceptReservation(@PathVariable long id) {
		Reservation reservation = reservationService.findReservationById(id);
		reservation.setStatus(1);
		reservation.getBorrowBook().setStatus(0);
		reservation.getBorrowBook().setBorrowDate(new Date(System.currentTimeMillis()));
		reservation.getBorrowBook().setExpirationDate(addDayToJavaUtilDate(new Date(System.currentTimeMillis()), 90));
		return ResponseEntity.ok("Success");
	}
	
	public Date addDayToJavaUtilDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.DAY_OF_YEAR, days);
	    return calendar.getTime();
	}
}
