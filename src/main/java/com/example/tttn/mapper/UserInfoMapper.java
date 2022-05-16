package com.example.tttn.mapper;

import com.example.tttn.entity.User;
import com.example.tttn.payload.request.UserInfo;

public class UserInfoMapper {
	
	public static User toUser(User user,UserInfo request) {
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setDateOfBirth(request.getDateOfBirth());
		return user;
	}
}
