package com.example.tttn.payload.response;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Profile {

	private long id;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String dateOfBirth;
	private Set<String> roles;
	private String createDate;
	private String status;
	public Profile(long id, String firstName, String lastName, String username, String email, String dateOfBirth,
			Set<String> roles, String createDate, String status) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.roles = roles;
		this.createDate = createDate;
		this.status = status;
	}
}
