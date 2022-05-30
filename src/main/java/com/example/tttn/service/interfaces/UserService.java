package com.example.tttn.service.interfaces;

import java.util.List;
import java.util.Map;

import com.example.tttn.entity.User;

public interface UserService {
	public User getUserByUsername(String username);
	public List<User> getAllUsers();
	public void saveUser(User user);
	public User getUserById(long id);
	public void deleteById(long id);
	public boolean existByUsername(String username);
	public List<User> searchByFirstName(String firstName);
	public Map<Integer, Integer> userInMonth(Long year, Long month);
}
