package com.example.tttn.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowBookResponse {

	private Long id;
	private String username;
	private String title;
	private String borrowDate;
	private String expirationDate;
	private String returnDate;
	private long penalty;
	private String status;
}
