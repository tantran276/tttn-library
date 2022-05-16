package com.example.tttn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tttn.entity.User;
import com.example.tttn.repository.UserRepository;
import com.example.tttn.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username).get();
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}

	@Override
	public User getUserById(long id) {
		return userRepository.getById(id);
	}

	@Override
	public void deleteById(long id) {
		userRepository.deleteById(id);
	}
	
}
