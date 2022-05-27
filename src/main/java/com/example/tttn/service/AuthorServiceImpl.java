package com.example.tttn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tttn.entity.Author;
import com.example.tttn.repository.AuthorRepository;
import com.example.tttn.service.interfaces.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService{

	@Autowired
	AuthorRepository authorRepository;
	
	@Override
	public List<Author> searchAuthor(String query) {
		return authorRepository.searchAuthor(query);
	}

}
