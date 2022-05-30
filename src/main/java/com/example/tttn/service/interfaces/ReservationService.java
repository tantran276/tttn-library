package com.example.tttn.service.interfaces;

import java.util.List;

import com.example.tttn.entity.Reservation;
import com.example.tttn.entity.User;

public interface ReservationService {
	List<Reservation> getAll();

	void saveReservation(Reservation reservation);

	Reservation findReservationById(long id);

	void deleteReservation(long id);

	long countReservationByUser(User user);

	
}
