package com.example.demo.service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.db.entity.Book;
import com.example.demo.db.repository.BookRepository;
import com.example.demo.google.GoogleBookService;
import com.example.demo.google.GoogleVolume;

@Service
public class BookService {
	
	private final GoogleBookService googleBookService;
	
	private final BookRepository bookRepository;

	@Autowired
	public BookService(GoogleBookService googleBookService, BookRepository bookRepository) {
		this.googleBookService = googleBookService;
		this.bookRepository = bookRepository;
	}
	
	
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}
	
	
	public ResponseEntity<?> addBook(String googleId) {

		GoogleVolume volume = googleBookService.getVolumeById(googleId);

		Book book = new Book(volume.id(), volume.volumeInfo().title(), volume.volumeInfo().authors().stream().collect(Collectors.joining(", ")), volume.volumeInfo().pageCount());
	
		bookRepository.save(book);

		URI location = URI.create("/books");
		return ResponseEntity.created(location).body(volume);

	}
	
	
	
	

}
