package com.example.tttn.restcontroller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tttn.entity.Author;
import com.example.tttn.entity.Book;
import com.example.tttn.entity.BorrowBook;
import com.example.tttn.entity.Publisher;
import com.example.tttn.entity.Tag;
import com.example.tttn.mapper.BookDtoMapper;
import com.example.tttn.mapper.BookResponseMapper;
import com.example.tttn.payload.BookDto;
import com.example.tttn.payload.response.BookResponse;
import com.example.tttn.repository.AuthorRepository;
import com.example.tttn.repository.CategoryRepository;
import com.example.tttn.repository.PublisherRepository;
import com.example.tttn.repository.TagRepository;
import com.example.tttn.service.interfaces.AuthorService;
import com.example.tttn.service.interfaces.BookService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/books")
public class BookController {

	@Autowired
	BookService bookService;

	@Autowired
	AuthorRepository authorRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	PublisherRepository publisherRepository;

	@Autowired
	TagRepository tagRepository;

	@Autowired
	AuthorService authorService;

	@GetMapping()
	public ResponseEntity<?> getAllWithsPagination(@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "pageSize", required = false) Integer pageSize) {
		if (offset == null) {
			offset = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		List<BookDto> bookDtos = new ArrayList<>();
		bookService.getAll().forEach((book) -> {
			bookDtos.add(BookDtoMapper.toBookDto(book));
		});
		return ResponseEntity.ok(this.paging(bookDtos, offset, pageSize));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getBookById(@PathVariable long id) {

		return ResponseEntity.ok(BookDtoMapper.toBookDto(bookService.findById(id)));
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchBook(@RequestParam(name = "author", required = false) String author,
			@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "tag", required = false) String tag,
			@RequestParam(name = "isbn", required = false) String isbn,
			@RequestParam(name = "cspl", required = false) String cspl,
			@RequestParam(name = "year", required = false) Integer year,
			@RequestParam(name = "quicksearch", required = false) String query,
			@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "pageSize", required = false) Integer pageSize) {
		if (offset == null) {
			offset = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		if (query != null) {
			return ResponseEntity.ok(this.paging(this.convertListBook(bookService.searchBook(query)), offset, pageSize));
		}
		if (author != null) {
			return ResponseEntity.ok(this.paging(this.convertListBook(this.searchByAuthor(author)), offset, pageSize));
		}
		if (title != null) {
			return ResponseEntity.ok(this.paging(this.convertListBook(bookService.searchByTitle(title)), offset, pageSize));
		}
		if (isbn != null) {
			return ResponseEntity.ok(this.paging(this.convertListBook(bookService.findBookByIsbn(isbn)), offset, pageSize));
		}
		if (cspl != null) {
			return ResponseEntity.ok(this.paging(this.convertListBook(this.searchByCategory(cspl)), offset, pageSize));
		}
		if (tag != null) {
			return ResponseEntity.ok(this.paging(this.convertListBook(this.searchByTag(tag)), offset, pageSize));
		}
		if (year != null) {
			return ResponseEntity.ok(this.paging(this.convertListBook(bookService.searchByYear(year)), offset, pageSize));
		}
		return ResponseEntity.ok(new PageImpl<>(new ArrayList(), PageRequest.of(offset, pageSize), 0) );
	}

	@GetMapping("/book")
	public ResponseEntity<?> getAllBookWithsPagination(@RequestParam(name = "offset", required = false) Integer offset,
			@RequestParam(name = "pageSize", required = false) Integer pageSize) {
		if (offset == null) {
			offset = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		List<BookResponse> bookResponses = new ArrayList<>();
		bookService.getAllByIsbn().forEach((book) -> {
			bookResponses.add(this.toBookResponse(book));
		});
		return ResponseEntity.ok(this.paging(bookResponses, offset, pageSize));
	}
	
	@GetMapping("/count")
	public ResponseEntity<?> countBook() {
		return ResponseEntity.ok(bookService.countBook());
	}

	@PostMapping
	public ResponseEntity<?> createBook(@RequestBody BookDto bookDto) throws ParseException {
		Book book = new Book();
		book.setStatus(true);
		Book book1 = bookService.saveBook(this.toBook(book, bookDto));
		return ResponseEntity.ok(BookDtoMapper.toBookDto(book1));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable long id) {
		List<BorrowBook> borrowBooks = bookService.findById(id).getBorrowBooks();
		if(borrowBooks.isEmpty()) {
			bookService.deleteById(id);
			return ResponseEntity.ok("Success");
		}
		for (int i = 0; i < borrowBooks.size(); i++) {
			if(borrowBooks.get(i).getReservation().getStatus()==0) {
				return ResponseEntity.badRequest().body("Sach dang duoc dang ky muon");
			}else if(borrowBooks.get(i).getReservation().getStatus() == 1 && (borrowBooks.get(i).getStatus() == 0 || borrowBooks.get(i).getStatus() ==1 )) {
				return ResponseEntity.badRequest().body("Sach dang duoc muon");
			} else {
				bookService.deleteById(id);
				return ResponseEntity.ok("Success");
			}
		}
		return ResponseEntity.ok("Success");
	}

	@PutMapping
	public ResponseEntity<?> updateBook(@RequestBody BookDto bookDto) {
		List<Book> books = bookService.findBookByIsbn(bookDto.getIsbn());
		books.forEach(book -> {
			try {
				this.toBook(book, bookDto);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			bookService.saveBook(book);
		});
		return ResponseEntity.ok("Success");
	}

	@GetMapping("/image/{isbn}")
	public ResponseEntity<Resource> downloadFile(@PathVariable(name = "isbn") String isbn) {
		byte[] image = null;
		image = bookService.getImage(isbn);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new ByteArrayResource(image));
	}

	@PutMapping("image/{isbn}")
	public ResponseEntity<?> uploadFile(@PathVariable(name = "isbn") String isbn,
			@RequestParam("file") MultipartFile file) {
		if (file.getName().contains("..")) {
			return ResponseEntity.badRequest().body("File khong hop le");
		}
		bookService.findBookByIsbn(isbn).forEach((book) -> {
			try {
				book.setImage(file.getBytes());
				bookService.saveBook(book);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return ResponseEntity.ok("Success");
	}

	public Book toBook(Book book, BookDto bookDto) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Set<Author> authors = new HashSet<>();
		bookDto.getAuthors().forEach((author) -> {
			if (authorRepository.existsByName(author)) {
				authors.add(authorRepository.findByName(author));
			} else {
				authorRepository.save(new Author(author, Arrays.asList(book)));
				authors.add(authorRepository.findByName(author));
			}
		});
		book.setAuthors(authors);
		book.setTitle(bookDto.getTitle());
		book.setCategory(categoryRepository.findByName(bookDto.getCategory()));
		book.setContent(bookDto.getContent());
		book.setCreateDate(formatter.parse(bookDto.getCreateDate()));
		book.setIsbn(bookDto.getIsbn());
		book.setPrice(bookDto.getPrice());
		if (publisherRepository.existsByName(bookDto.getPublisher())) {
			book.setPublisher(publisherRepository.findByName(bookDto.getPublisher()));
		} else {
			publisherRepository.save(new Publisher(bookDto.getPublisher(), new ArrayList<Book>(Arrays.asList(book))));
			book.setPublisher(publisherRepository.findByName(bookDto.getPublisher()));
		}
		Set<Tag> tags = new HashSet<>();
		bookDto.getTags().forEach((tag) -> {
			if (tagRepository.existsByName(tag)) {
				tags.add(tagRepository.findByName(tag));
			} else {
				tagRepository.save(new Tag(tag, Arrays.asList(book)));
				tags.add(tagRepository.findByName(tag));
			}
		});
		book.setTags(tags);
		book.setTitle(bookDto.getTitle());
		return book;
	}

	public Page<?> paging(List<?> list, int offset, int pageSize) {
		Pageable pageable = PageRequest.of(offset, pageSize);
		if (list.size() <= (offset * pageSize)) {
			return new PageImpl<>(new ArrayList<>(), PageRequest.of(offset, pageSize), list.size());
		}
		final int toIndex = Math.min((pageable.getPageNumber() + 1) * pageable.getPageSize(), list.size());
		final int fromIndex = offset*pageSize;
		System.out.println(list.size());
		return new PageImpl<>(list.subList(fromIndex, toIndex), pageable, list.size());
	}

	public List<Book> searchByAuthor(String query) {
		List<Book> books = new ArrayList<>();
		authorService.searchAuthor(query).forEach((author) -> {
			books.addAll(author.getBooks());
		});
		return books;
	}

	private List<Book> searchByCategory(String cspl) {
		List<Book> books = new ArrayList<>();
		categoryRepository.searchCategory(cspl).forEach((category) -> {
			books.addAll(category.getBooks());
		});
		return books;
	}

	private List<Book> searchByTag(String tag) {
		List<Book> books = new ArrayList<>();
		tagRepository.searchTag(tag).forEach((t) -> {
			books.addAll(t.getBooks());
		});
		return books;
	}

	public List<BookResponse> convertListBook(List<Book> books) {
		List<BookResponse> bookResponses = new ArrayList<>();
		books.forEach((book) -> {
			BookResponse bookResponse = BookResponseMapper.toBookResponse(book);
			if (!bookResponses.contains(bookResponse)) {
				bookResponses.add(bookResponse);
			}
			long borrowed = bookService.countBorrowedBook(book.getIsbn());
			bookResponse.setBorrowed(borrowed);
			long ready = bookService.countReadyBook(book.getIsbn());
			bookResponse.setReady(ready);
		});
		return bookResponses;
	}

	public BookResponse toBookResponse(Book book) {
		BookResponse bookResponse = BookResponseMapper.toBookResponse(book);
		long borrowed = bookService.countBorrowedBook(book.getIsbn());
		bookResponse.setBorrowed(borrowed);
		long ready = bookService.countReadyBook(book.getIsbn());
		bookResponse.setReady(ready);
		return bookResponse;
	}
}
