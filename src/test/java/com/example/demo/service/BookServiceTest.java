package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.example.demo.db.entity.Book;
import com.example.demo.db.repository.BookRepository;
import com.example.demo.google.GoogleBookService;
import com.example.demo.google.GoogleVolume;
import com.example.demo.google.GoogleVolume.GoogleVolumeInfo;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BookServiceTest {


    @Mock
    GoogleBookService googleBookService;

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    @Test
    void addBook_returns201Created_setsLocation_andPersistsBook() {
    	
        String googleId = "abc123";
        GoogleVolumeInfo info = new GoogleVolumeInfo("Effective Java", List.of("Joshua Bloch"), 416);
        GoogleVolume volume = new GoogleVolume(googleId, info);

        when(googleBookService.getVolumeById(googleId)).thenReturn(volume);
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<?> response = bookService.addBook(googleId);

        assertThat(response.getStatusCode().value()).isEqualTo(201);

        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("/books"));

        assertThat(response.getBody()).isEqualTo(volume);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book saved = bookCaptor.getValue();
        assertThat(saved.getId()).isEqualTo("abc123");
        assertThat(saved.getTitle()).isEqualTo("Effective Java");
        assertThat(saved.getAuthor()).isEqualTo("Joshua Bloch");
        assertThat(saved.getPageCount()).isEqualTo(416);

        verify(googleBookService).getVolumeById(googleId);
        verifyNoMoreInteractions(googleBookService, bookRepository);
    }

}
