package com.example.tttn.payload;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
	private Long id;
	private String isbn;
	private String title;
	private Set<String> tags;
	private Set<String> authors;
	private String publisher;
	private String category;
	private String content;
	private long price;
	private String createDate;
	private String status;
}
