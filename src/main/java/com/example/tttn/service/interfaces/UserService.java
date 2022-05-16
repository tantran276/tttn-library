package com.example.tttn.service.interfaces;

import java.util.List;

import com.example.tttn.entity.User;

public interface UserService {
	public User getUserByUsername(String username);
	public List<User> getAllUsers();
	public void saveUser(User user);
	public User getUserById(long id);
	public void deleteById(long id);
}
