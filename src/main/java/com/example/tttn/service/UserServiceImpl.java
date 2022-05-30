package com.example.tttn.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public boolean existByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public List<User> searchByFirstName(String firstName) {
		return userRepository.searchUserByFirstName(firstName);
	}

	@Override
	public Map<Integer, Integer> userInMonth(Long year, Long month) {
		Map<Integer, Integer> userInMonthMap = new HashMap();
		List<Integer> userInMonthDays = userRepository.searchDayNumberUserForMonth(year, month);
		List<Integer> numberUserInMonths = userRepository.searchNumberUserForMonth(year, month);
		for (int i = 0; i < userInMonthDays.size(); i++) {
			userInMonthMap.put(userInMonthDays.get(i), numberUserInMonths.get(i));
		}
		return userInMonthMap;
	}
	
}
