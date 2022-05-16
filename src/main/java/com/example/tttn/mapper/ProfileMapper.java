package com.example.tttn.mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.tttn.entity.Role;
import com.example.tttn.entity.User;
import com.example.tttn.payload.response.Profile;

public class ProfileMapper {
	public static Profile toProfile(User user) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String role = "";
		String role1 = "";
		List<Role> roles = new ArrayList<>(user.getRoles());
		if (roles.get(0) != null) {
			role = roles.get(0).getName().name();
		}
		if (roles.size()>= 2) {
			if (roles.get(1) != null) {
				role1 = roles.get(1).getName().name();
			}
		}
		return new Profile(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(),
				formatter.format(user.getDateOfBirth()), role, role1, formatter.format(user.getCreateDate()));

	}
}
