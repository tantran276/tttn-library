package com.example.tttn.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class LoginRequest {
	private String username;
	private String password;
}
