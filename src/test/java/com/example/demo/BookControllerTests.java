package com.example.demo;

import com.example.demo.db.entity.Book;
import com.example.demo.db.repository.BookRepository;
import com.example.demo.service.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookControllerTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookService bookService;

	@BeforeEach
	void setup() {
		bookRepository.deleteAll();
		bookRepository.save(new Book("lRtdEAAAQBAJ", "Spring in Action", "Craig Walls"));
		bookRepository.save(new Book("12muzgEACAAJ", "Effective Java", "Joshua Bloch"));
	}

	@Test
	void testGetAllBooks() throws Exception {
		mockMvc.perform(get("/books")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Spring in Action"))
				.andExpect(jsonPath("$[1].title").value("Effective Java"));
	}

	@Test
	void testAddBook_201() throws Exception {
	    String googleId = "piOyzYqeZGgC";

	    mockMvc.perform(post("/books/" + googleId))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.id").value(googleId))
	            .andExpect(jsonPath("$.volumeInfo").exists())
	            .andExpect(jsonPath("$.volumeInfo.title").value("Effective Java"))
	            .andExpect(jsonPath("$.volumeInfo.authors[0]").value("Joshua Bloch"))
	            .andExpect(jsonPath("$.volumeInfo.pageCount").value(265));
	}
	
	@Test
	void testAddBook_400() throws Exception {

	    mockMvc.perform(post("/books/ "))
	            .andExpect(status().isBadRequest())
	            .andExpect(jsonPath("$.error").exists())
	            .andExpect(jsonPath("$.message")
	                    .value("Google ID must not be null or blank"));
	}

}
