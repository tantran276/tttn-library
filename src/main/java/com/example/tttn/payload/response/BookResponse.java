package com.example.tttn.payload.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

	private String isbn;
	private String title;
	private Set<String> tags;
	private Set<String> authors;
	private String publisher;
	private String category;
	private String content;
	private long price;
	private String createDate;
	private long borrowed;
	private long ready;
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() != this.getClass()) {
            return false;
        }
		BookResponse bookResponse = (BookResponse) obj;
		return this.getIsbn().equals(bookResponse.getIsbn());
	}

	@Override
	public int hashCode() {
		return this.getIsbn().hashCode();
	}
	
	
	
	
}
