package com.example.tttn.mapper;

import java.text.SimpleDateFormat;

import com.example.tttn.entity.Reservation;
import com.example.tttn.payload.response.ReservationResponse;

public class ReservationResponseMapper {

	public static ReservationResponse toReservationResponse(Reservation reservation) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ReservationResponse response = new ReservationResponse();
		response.setId(reservation.getId());
		response.setBook(reservation.getBorrowBook().getBook().getTitle());
		response.setExpirationDate(formatter.format(reservation.getExpirationDate()));
		response.setReservationDate(formatter.format(reservation.getReservationDate()));
		response.setUsername(reservation.getUser().getUsername());
		if(reservation.getStatus() == -1) {
			response.setStatus("Quá thời hạn đăng ký mượn");
		}
		if(reservation.getStatus() == 0) {
			response.setStatus("Đang đang ký mượn");
		}
		if(reservation.getStatus() == 1) {
			response.setStatus("Đã cho mượn");
		}
		return response;
	}
}
