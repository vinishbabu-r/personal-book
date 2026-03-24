package com.example.demo.google;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test using a mock server to simulate Google Books API.
 * Serves the JSON from src/test/resources/effectivejava.json.
 */
@SpringBootTest
class GoogleBookServiceMockServerTests {

    static MockWebServer server;

    @BeforeAll
    static void startServer() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    static void stopServer() throws IOException {
        server.shutdown();
    }

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("google.books.base-url", () -> server.url("/").toString());
    }

    @Autowired
    private GoogleBookService googleBookService;

    @Test
    void search_mocked_returnsEffectiveJava() throws IOException {
    	 Path path = Paths.get("src", "test", "resources", "effectivejava.json");
         String body = Files.readString(path);
         server.enqueue(new MockResponse()
                 .setResponseCode(200)
                 .addHeader("Content-Type", "application/json")
                 .setBody(body));
        GoogleBook result = googleBookService.searchBooks("effective+java", 5, 0);
        assertThat(result).isNotNull();
        assertThat(result.kind()).isEqualTo("books#volumes");
        assertThat(result.items()).isNotEmpty();
        GoogleBook.Item first = result.items().get(0);
        assertThat(first.volumeInfo().title()).isEqualTo("Effective Java");
    }
    
    @Test
    void search_mocked_returnsVolume()throws IOException {

    	Path path = Paths.get("src", "test", "resources", "volumn.json");
        String body = Files.readString(path);
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(body));

        GoogleVolume result = googleBookService.getVolumeById("piOyzYqeZGgC");
        System.out.println("return: "+result.toString());
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("piOyzYqeZGgC");
        assertThat(result.volumeInfo()).isNotNull();
        assertThat(result.volumeInfo().title()).isEqualTo("Effective Java");
        assertThat(result.volumeInfo().authors().get(0)).isEqualTo("Joshua Bloch");
        assertThat(result.volumeInfo().pageCount()).isEqualTo(260);

    }
    
}
