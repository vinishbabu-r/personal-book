package com.example.demo.google;

import java.util.List;

public record GoogleVolume(String id, GoogleVolumeInfo volumeInfo) {
	public record GoogleVolumeInfo(String title, List<String> authors, Integer pageCount) {
	}
}