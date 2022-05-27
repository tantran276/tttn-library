package com.example.tttn.payload.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SignupRequest {
	
	private final String firstName;
	private final String lastName;
	private final String username;
	private final String email;
	private final String password;
	private final Date dateOfBirth;
	private final String siteUrl;
}
