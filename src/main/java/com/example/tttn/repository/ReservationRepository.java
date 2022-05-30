package com.example.tttn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tttn.entity.Reservation;
import com.example.tttn.entity.User;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("Select r from Reservation r where r.user = ?1 " + "And r.status = 0")
	long countResevationByUser(User user);

}