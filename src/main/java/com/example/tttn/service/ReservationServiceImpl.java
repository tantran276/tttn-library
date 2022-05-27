package com.example.tttn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tttn.entity.Reservation;
import com.example.tttn.entity.User;
import com.example.tttn.repository.ReservationRepository;
import com.example.tttn.service.interfaces.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService{

	@Autowired 
	ReservationRepository reservationRepository;

	@Override
	public List<Reservation> getAll() {
		return reservationRepository.findAll();
	}

	@Override
	public void saveReservation(Reservation reservation) {
		reservationRepository.save(reservation);
	}

	@Override
	public Reservation findReservationById(long id) {
		return reservationRepository.getById(id);
	}

	@Override
	public void deleteReservation(long id) {
		reservationRepository.deleteById(id);;
	}

	@Override
	public long countReservationByUser(User user) {
		return reservationRepository.countResevationByUser(user);
	}
	
}
