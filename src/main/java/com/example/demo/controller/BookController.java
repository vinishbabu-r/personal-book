package com.example.demo.controller;

import com.example.demo.db.entity.Book;
import com.example.demo.db.repository.BookRepository;
import com.example.demo.google.GoogleBook;
import com.example.demo.google.GoogleBookService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
public class BookController {
	private final BookRepository bookRepository;
	private final GoogleBookService googleBookService;

	@Autowired
	public BookController(BookRepository bookRepository, GoogleBookService googleBookService) {
		this.bookRepository = bookRepository;
		this.googleBookService = googleBookService;
	}

	@GetMapping("/books")
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@GetMapping("/google")
	public GoogleBook searchGoogleBooks(@RequestParam("q") String query,
			@RequestParam(value = "maxResults", required = false) Integer maxResults,
			@RequestParam(value = "startIndex", required = false) Integer startIndex) {
		return googleBookService.searchBooks(query, maxResults, startIndex);
	}
}
