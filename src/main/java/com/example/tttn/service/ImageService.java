package com.example.tttn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tttn.repository.UserRepository;

@Service
public class ImageService {

	@Autowired
    UserRepository userRepository;
}
