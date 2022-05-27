package com.example.tttn.mapper;

import java.text.SimpleDateFormat;

import com.example.tttn.entity.BorrowBook;
import com.example.tttn.payload.response.BorrowBookResponse;

public class BorrowBookResponseMapper {
	public static BorrowBookResponse toBorrowBookResponse(BorrowBook borrowBook) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		BorrowBookResponse borrowBookResponse = new BorrowBookResponse();
		borrowBookResponse.setId(borrowBook.getId());
		borrowBookResponse.setUsername(borrowBook.getReservation().getUser().getUsername());
		borrowBookResponse.setTitle(borrowBook.getBook().getTitle());
		if (borrowBook.getReturnDate() != null) {
			borrowBookResponse.setReturnDate(formatter.format(borrowBook.getReturnDate()));
		}
		borrowBookResponse.setBorrowDate(formatter.format(borrowBook.getBorrowDate()));
		borrowBookResponse.setExpirationDate(formatter.format(borrowBook.getExpirationDate()));
		borrowBookResponse.setPenalty(borrowBook.getPenalty());
		if(borrowBook.getStatus() == -2) {
			borrowBookResponse.setStatus("Đang đăng ký mượn");
		}
		if(borrowBook.getStatus() == -1) {
			borrowBookResponse.setStatus("Quá hạn");
		}
		if(borrowBook.getStatus() == 0) {
			borrowBookResponse.setStatus("Đang mượn");
		}
		if(borrowBook.getStatus() == 1) {
			borrowBookResponse.setStatus("Đã trả");
		}
		return borrowBookResponse;
	}
}
