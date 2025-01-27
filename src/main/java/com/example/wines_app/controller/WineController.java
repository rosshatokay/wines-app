package com.example.wines_app.controller;

import com.example.wines_app.dto.WineDto;
import com.example.wines_app.models.Wine;
import com.example.wines_app.service.WineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller

public class WineController {
	private WineService wineService;

	@Autowired
	public WineController(WineService wineService) {
		this.wineService = wineService;
	}

	@GetMapping("/")
	public String getIndex() {
		return "index";
	}

	@GetMapping("/wines/all")
	public String getWines() {
		return "wines-list";
	}

	@GetMapping("/wines/red")
	public String getRedWines() {
		return "red";
	}

	@GetMapping("/wines/white")
	public String getWhiteWines() {
		return "white";
	}

	@GetMapping("/wines/compare")
	public String getCompareWines() {
		return "compare";
	}

	@GetMapping("/api/wines")
	@ResponseBody
	public List<Map<String, Object>> listWines(
			@RequestParam(required = false) String color,
			@RequestParam(required = false) List<String> fields,
			@RequestParam(required = false) String start_date,
			@RequestParam(required = false) String end_date) {
		if (fields == null || fields.isEmpty()) {
			fields = Arrays.stream(Wine.class.getDeclaredFields())
					.map(Field::getName)
					.toList();
		}

		return wineService.findWines(color, fields, start_date, end_date);
	}

	@GetMapping("/api/wines/split-by-color")
	@ResponseBody
	// public Map<String, List<WineDto>> listWinesByColor() {
	public Map<String, Long> listWinesByColor() {
		List<WineDto> wines = wineService.findAllWines();

		// Split the list into two based on color
		long redWinesCount = wines.stream()
				.filter(wine -> "red".equalsIgnoreCase(wine.getColor()))
				.count();

		long whiteWinesCount = wines.stream()
				.filter(wine -> "white".equalsIgnoreCase(wine.getColor())).count();

		// Return a JSON object with two keys
		return Map.of("redCount", redWinesCount, "whiteCount", whiteWinesCount);
	}

	@GetMapping("/api/multi-query")
	@ResponseBody
	public Map<String, List<Map<String, Object>>> runMultiQuery() {
		List<String> fields = Arrays.asList("color", "pH", "alcohol");

		// Run two queries concurrently
		CompletableFuture<List<Map<String, Object>>> query1 = CompletableFuture.supplyAsync(() -> wineService.findWines("red", fields, null, null));
		CompletableFuture<List<Map<String, Object>>> query2 = CompletableFuture.supplyAsync(() -> wineService.findWines("white", fields, null, null));

		// Combine results
		CompletableFuture.allOf(query1, query2).join();

		return Map.of(
				"redWines", query1.join(),
				"whiteWines", query2.join()
		);
	}
}
