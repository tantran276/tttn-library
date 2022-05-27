package com.example.tttn.mapper;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import com.example.tttn.entity.Book;
import com.example.tttn.payload.response.BookResponse;

public class BookResponseMapper {
	public static BookResponse toBookResponse(Book book) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		BookResponse bookResponse = new BookResponse();
		Set<String> authors = new HashSet<>();
		book.getAuthors().forEach((author) -> {
			authors.add(author.getName());
		});
		bookResponse.setAuthors(authors);
		bookResponse.setCategory(book.getCategory().getCategoryName());
		bookResponse.setContent(book.getContent());
		bookResponse.setCreateDate(formatter.format(book.getCreateDate()));
		bookResponse.setIsbn(book.getIsbn());
		bookResponse.setPrice(book.getPrice());
		bookResponse.setPublisher(book.getPublisher().getName());
		Set<String> tags = new HashSet<>();
		book.getTags().forEach((tag) -> {
			tags.add(tag.getName());
		});
		bookResponse.setTags(tags);
		bookResponse.setTitle(book.getTitle());
		return bookResponse;
	}
}