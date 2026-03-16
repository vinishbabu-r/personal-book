package com.example.demo.controller;

import com.example.demo.db.entity.Book;
import com.example.demo.google.GoogleBook;
import com.example.demo.google.GoogleBookService;
import com.example.demo.service.BookService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class BookController {
	private final BookService bookService;
	private final GoogleBookService googleBookService;

	@Autowired
	public BookController(BookService bookService, GoogleBookService googleBookService) {
		this.bookService = bookService;
		this.googleBookService = googleBookService;
	}

	@GetMapping("/books")
	public List<Book> getAllBooks() {
		return bookService.getAllBooks();
	}

	@GetMapping("/google")
	public GoogleBook searchGoogleBooks(@RequestParam("q") String query,
			@RequestParam(value = "maxResults", required = false) Integer maxResults,
			@RequestParam(value = "startIndex", required = false) Integer startIndex) {
		return googleBookService.searchBooks(query, maxResults, startIndex);
	}
	
	@PostMapping("/books/{googleId}")
	public ResponseEntity<?> addBook(@PathVariable String googleId) {
		return bookService.addBook(googleId);
	}
}
