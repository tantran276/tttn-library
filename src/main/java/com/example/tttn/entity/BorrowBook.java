package com.example.tttn.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrow_book")
public class BorrowBook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@JsonIgnore
	@OneToOne(mappedBy = "borrowBook")
    private Reservation reservation;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    private Book book;
	
	@Column(name = "borrow_date")
	private Date borrowDate;
	
	@Column(name = "expiration_date")
	private Date expirationDate;
	
	@Column(name = "return_date")
	private Date returnDate;
	
	@Column(name = "penalty")
	private long penalty;
	
	@Column(name = "status")
	private long status;
}
