package com.example.demo.service;

import java.net.URI;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.db.entity.Book;
import com.example.demo.db.repository.BookRepository;
import com.example.demo.google.GoogleBookService;
import com.example.demo.google.GoogleVolume;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookService {
	
	private final GoogleBookService googleBookService;
	
	private final BookRepository bookRepository;

	@Autowired
	public BookService(GoogleBookService googleBookService, BookRepository bookRepository) {
		this.googleBookService = googleBookService;
		this.bookRepository = bookRepository;
	}
	
	
	@Transactional
	public ResponseEntity<?> addBook(String googleId) {

		GoogleVolume volume = googleBookService.getVolumeById(googleId);

		Book book = new Book(volume.id(), volume.volumeInfo().title(), volume.volumeInfo().authors().stream().collect(Collectors.joining(", ")), volume.volumeInfo().pageCount());
	
		bookRepository.save(book);
		
		log.info("Created Book, ID - "+googleId);

		URI location = URI.create("/books");
		return ResponseEntity.created(location).body(volume);

	}
	
	
	
	

}
