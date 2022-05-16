package com.example.tttn.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

	private long id;
	private String username;
	private String book;
	private String reservationDate;
	private String expirationDate;
	private String status;
}
