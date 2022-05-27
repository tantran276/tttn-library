package com.example.tttn.mapper;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import com.example.tttn.entity.User;
import com.example.tttn.payload.response.Profile;

public class ProfileMapper {
	public static Profile toProfile(User user) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Set<String> roles = new HashSet();
		user.getRoles().forEach((role) -> {
			roles.add(role.getName().name());
		});
		String status =null;
		if(user.isEnabled()) {
			status = "Đã xác thực";
		} else {
			status = "Chưa xác thực";
		}
		return new Profile(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(),
				formatter.format(user.getDateOfBirth()), roles, formatter.format(user.getCreateDate()), status);

	}
}
