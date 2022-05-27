package com.example.tttn.service.interfaces;

import java.util.List;

import com.example.tttn.entity.Author;

public interface AuthorService {

	List<Author> searchAuthor(String query);

}
