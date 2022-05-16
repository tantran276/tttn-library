package com.example.tttn.payload.response;

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
	private String role;
	private String role1;
	private String createDate;
	public Profile(long id, String firstName, String lastName, String username, String email, String dateOfBirth,
			String role, String role1, String createDate) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.role = role;
		this.role1 = role1;
		this.createDate = createDate;
	}

	
}
