package com.example.tttn.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResetPasswordRequest {

	private String code;
	private String newPassword;
}
