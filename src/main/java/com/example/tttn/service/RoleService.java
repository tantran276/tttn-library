package com.example.tttn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tttn.entity.ERole;
import com.example.tttn.entity.Role;
import com.example.tttn.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	public Role getByName(String name) {
		if (name.equals(ERole.USER.name())) {
			Role role = roleRepository.findByName(ERole.USER).get();
			return role;
		}
		if (name.equals(ERole.ADMIN.name())) {
			Role role = roleRepository.findByName(ERole.ADMIN).get();
			return role;
		}
		return null;
	}
}
