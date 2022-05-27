package com.example.tttn;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.tttn.service.interfaces.BorrowBookService;
import com.example.tttn.service.interfaces.ReservationService;

@SpringBootApplication
@EnableScheduling
public class TttnLibraryManagerment3Application {
	public static void main(String[] args) {
		SpringApplication.run(TttnLibraryManagerment3Application.class, args);
	}
	
	@Autowired
	ReservationService reservationService;
	
	@Autowired
	BorrowBookService borrowBookService;
	
	@Scheduled(cron  = "0 0 8,18 * * *")
	void someJob() {
		reservationService.getAll().forEach((reservation) -> {
			if(reservation.getStatus() == 0) {
				if(reservation.getExpirationDate().before(new Date(System.currentTimeMillis()))) {
					reservation.setStatus(-1);
					reservation.getBorrowBook().setStatus(1);
					reservation.getBorrowBook().getBook().setStatus(true);
					reservationService.saveReservation(reservation);
				}
			}
		});
		
		borrowBookService.getAll().forEach((borrowBook) -> {
			if(borrowBook.getStatus() == 0) {
				if(borrowBook.getExpirationDate().before(new Date(System.currentTimeMillis()))) {
					borrowBook.setStatus(-1);
					Long diff = System.currentTimeMillis() - borrowBook.getExpirationDate().getTime();
					long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					borrowBook.setPenalty(days*500);
					borrowBookService.saveBorrowBook(borrowBook);
				}
			}
		});
	}
}

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
class SchedulingConfiguration {
	
}