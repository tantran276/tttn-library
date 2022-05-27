package com.example.tttn.restcontroller;

import java.text.ParseException;
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
import com.example.tttn.entity.User;
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

	@GetMapping("/history")
	public ResponseEntity<?> getAllReservation(@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "pageSize", required = false) Integer pageSize) {
		if (offset == null) {
			offset = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		List<ReservationResponse> reservations = new ArrayList<ReservationResponse>();
		reservationService.getAll().forEach((reservation) -> {
			reservations.add(ReservationResponseMapper.toReservationResponse(reservation));
		});
		return ResponseEntity.ok(this.paging(reservations, offset, pageSize));
	}

	@GetMapping("/reserving/{user_id}")
	public ResponseEntity<?> getUserReserving(@RequestParam(name = "page", required = false) Integer offset,
			@RequestParam(name = "page", required = false) Integer pageSize, @PathVariable(name = "user_id") long id) {
		if (offset == null) {
			offset = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		User user = userService.getUserById(id);
		List<ReservationResponse> reservations = new ArrayList<ReservationResponse>();
		user.getReservations().forEach((reservation) -> {
			if (reservation.getStatus() == 0) {
				reservations.add(ReservationResponseMapper.toReservationResponse(reservation));
			}
		});
		return ResponseEntity.ok(this.paging(reservations, offset, pageSize));
	}

	@GetMapping("/reserving")
	public ResponseEntity<?> getAllReserving(@RequestParam(name = "page", required = false) Integer offset,
			@RequestParam(name = "page", required = false) Integer pageSize) {
		if (offset == null) {
			offset = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		List<ReservationResponse> reservations = new ArrayList<ReservationResponse>();
		reservationService.getAll().forEach((reservation) -> {
			if (reservation.getStatus() == 0) {
				reservations.add(ReservationResponseMapper.toReservationResponse(reservation));
			}
		});
		return ResponseEntity.ok(this.paging(reservations, offset, pageSize));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getAllReservation(@PathVariable long id) {
		ReservationResponse reservation = ReservationResponseMapper
				.toReservationResponse(reservationService.findReservationById(id));
		return ResponseEntity.ok(reservation);
	}

	@PostMapping()
	public ResponseEntity<?> createReservationBook(@RequestBody ReservationRequest request) {
		User user = userService.getUserById(request.getUserId());
		int countReservation = 0;
		if (!user.getReservations().isEmpty()) {
			for (int i = 0; i < user.getReservations().size(); i++) {
				if (user.getReservations().get(i).getStatus() == 1) {
					if (user.getReservations().get(i).getBorrowBook().getStatus() == 0) {
						if (user.getReservations().get(i).getBorrowBook().getBook().getIsbn()
								.equals(request.getIsbn())) {
							return ResponseEntity.badRequest().body("Ban da muon quyen sach nay roi");
						}
						countReservation++;
					}
				}
				if (user.getReservations().get(i).getStatus() == 0) {
					if (user.getReservations().get(i).getBorrowBook().getBook().getIsbn().equals(request.getIsbn())) {
						return ResponseEntity.badRequest().body("Ban da muon quyen sach nay roi");
					}
					countReservation++;
				}
			}
		}
		System.out.println(countReservation);
		if (countReservation > 5) {
			return ResponseEntity.badRequest().body("Ban da dang ky muon va muon 5 quyen sach!");
		}
		System.out.println(request.getIsbn());
		List<Book> books = bookService.findReadyBookByIsbn(request.getIsbn());
		if (books.isEmpty()) {
			return ResponseEntity.badRequest().body("Sach da duoc muon het");
		}
		Book book = books.get(0);
		book.setStatus(false);
		BorrowBook borrowBook = new BorrowBook();
		borrowBook.setBook(book);
		borrowBook.setStatus(-2);
		Reservation reservation = new Reservation();
		reservation.setUser(user);
		reservation.setBorrowBook(borrowBook);
		reservation.setReservationDate(new Date(System.currentTimeMillis()));
		reservation.setExpirationDate(addDayToJavaUtilDate(reservation.getReservationDate(), 3));
		reservation.setStatus(0);
		reservationService.saveReservation(reservation);
		return ResponseEntity.ok(reservation);
	}
	
	@PostMapping("/borrow")
	public ResponseEntity<?> borrowBook(@RequestBody ReservationRequest request) {
		if(this.createReservationBook(request).getBody().getClass() != Reservation.class) {
			return ResponseEntity.badRequest().body(this.createReservationBook(request).getBody());
		}
		Reservation reservation = (Reservation) this.createReservationBook(request).getBody();
		reservationService.saveReservation(reservation);
		this.acceptReservation(reservation.getId());
		return ResponseEntity.ok("Success");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteReservation(@PathVariable long id) {
		Reservation reservation = reservationService.findReservationById(id);
		reservation.getBorrowBook().getBook().setStatus(true);
		reservationService.deleteReservation(id);
		return ResponseEntity.ok("Success");
	}

	@PutMapping("/cancel/{id}")
	public ResponseEntity<?> cancelReservation(@PathVariable long id) {
		Reservation reservation = reservationService.findReservationById(id);
		if (reservation.getStatus() == 1) {
			return ResponseEntity.badRequest().body("Dang ky muon da hoan tat!");
		}
		reservation.setStatus(-1);
		reservation.getBorrowBook().setStatus(1);
		reservation.getBorrowBook().getBook().setStatus(true);
		reservationService.saveReservation(reservation);
		return ResponseEntity.ok("Success");
	}
	
	
	@PutMapping("/admincancel/{id}")
	public ResponseEntity<?> adminCancelReservation(@PathVariable long id) {
		Reservation reservation = reservationService.findReservationById(id);
		if (reservation.getStatus() == 1) {
			return ResponseEntity.badRequest().body("Dang ky muon da hoan tat!");
		}
		reservation.setStatus(1);
		reservation.getBorrowBook().setStatus(1);
		reservation.getBorrowBook().getBook().setStatus(true);
		reservationService.saveReservation(reservation);
		return ResponseEntity.ok("Success");
	}

	@PutMapping("/renewal/{id}")
	public ResponseEntity<?> renewalReservation(@PathVariable long id) throws ParseException {
		Reservation reservation = reservationService.findReservationById(id);
		if (reservation.getStatus() == 1) {
			return ResponseEntity.badRequest().body("Dang ky muon da hoan tat!");
		}
		reservation.setExpirationDate(addDayToJavaUtilDate(reservation.getExpirationDate(), 3));
		reservationService.saveReservation(reservation);
		return ResponseEntity.ok("Success");
	}

	@PutMapping("/accept/{id}")
	public ResponseEntity<?> acceptReservation(@PathVariable long id) {
		System.out.println("accept");
		Reservation reservation = reservationService.findReservationById(id);
		reservation.setStatus(1);
		reservation.getBorrowBook().setStatus(0);
		reservation.getBorrowBook().setBorrowDate(new Date(System.currentTimeMillis()));
		reservation.getBorrowBook().setExpirationDate(addDayToJavaUtilDate(new Date(System.currentTimeMillis()), 90));
		reservationService.saveReservation(reservation);
		return ResponseEntity.ok("Success");
	}

	public Date addDayToJavaUtilDate(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}

	public Page<?> paging(List<?> list, int offset, int pageSize) {
		Pageable pageable = PageRequest.of(offset, pageSize);
		if (list.size() <= (offset * pageSize)) {
			return new PageImpl<>(new ArrayList<>(), PageRequest.of(offset, pageSize), list.size());
		}
		final int toIndex = Math.min((pageable.getPageNumber() + 1) * pageable.getPageSize(), list.size());
		final int fromIndex = offset * pageSize;
		System.out.println(list.size());
		return new PageImpl<>(list.subList(fromIndex, toIndex), pageable, list.size());
	}

}
