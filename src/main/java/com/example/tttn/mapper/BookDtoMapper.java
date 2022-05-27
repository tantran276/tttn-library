package com.example.tttn.mapper;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import com.example.tttn.entity.Book;
import com.example.tttn.payload.BookDto;

public class BookDtoMapper {
	public static BookDto toBookDto(Book book) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		BookDto bookDto = new BookDto();
		Set<String> authors = new HashSet<>();
		book.getAuthors().forEach((author) -> {
			authors.add(author.getName());
		});
		bookDto.setAuthors(authors);
		bookDto.setCategory(book.getCategory().getCategoryName());
		bookDto.setContent(book.getContent());
		bookDto.setCreateDate(formatter.format(book.getCreateDate()));
		bookDto.setId(book.getId());
		bookDto.setIsbn(book.getIsbn());
		bookDto.setPrice(book.getPrice());
		bookDto.setPublisher(book.getPublisher().getName());
		Set<String> tags = new HashSet<>();
		book.getTags().forEach((tag) -> {
			tags.add(tag.getName());
		});
		bookDto.setTags(tags);
		bookDto.setTitle(book.getTitle());
		if(book.isStatus()) {
			bookDto.setStatus("Đang rỗi");
		} else {
			bookDto.setStatus("Đang mượn");
		}
		
		return bookDto;
	}
}
