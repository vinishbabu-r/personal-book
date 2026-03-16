package com.example.demo.google;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.demo.exception.NotFoundException;

@Service
public class GoogleBookService {
	private final RestClient restClient;

	public GoogleBookService(@Value("${google.books.base-url:https://www.googleapis.com/books/v1}") String baseUrl) {
		this.restClient = RestClient.builder().baseUrl(baseUrl).build();
	}

	public GoogleBook searchBooks(String query, Integer maxResults, Integer startIndex) {
		return restClient.get()
				.uri(uriBuilder -> uriBuilder.path("/volumes").queryParam("q", query)
						.queryParam("maxResults", maxResults != null ? maxResults : 10)
						.queryParam("startIndex", startIndex != null ? startIndex : 0).build())
				.retrieve().body(GoogleBook.class);
	}
	
	public GoogleVolume getVolumeById(String volumeId) {
		return restClient.get().uri(uriBuilder -> uriBuilder.path("/volumes/{id}").build(volumeId)).retrieve()
				.onStatus(status -> status.value() != 200, (request, response) -> {
					String detail;
					try {
						detail = response.getBody() != null ? new String(response.getBody().readAllBytes())
								: "Resource not found";
					} catch (IOException e) {
						detail = "Resource not found";
					}
					throw new NotFoundException("Volume %s not found. %s".formatted(volumeId, detail));
				}).body(GoogleVolume.class);
	}
}
