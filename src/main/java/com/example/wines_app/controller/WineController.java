package com.example.wines_app.controller;

import com.example.wines_app.dto.WineDto;
import com.example.wines_app.service.WineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/api/wines")
    @ResponseBody
    public List<WineDto> listWinesByColor(@RequestParam(required = false) String color) {
        return wineService.findWinesByColor(color);
    }

    @GetMapping("/api/wines/split-by-color")
    @ResponseBody
//    public Map<String, List<WineDto>> listWinesByColor() {
    public Map<String, Long> listWinesByColor() {
        List<WineDto> wines = wineService.findAllWines();

        // Split the list into two based on color
        long redWinesCount = wines.stream()
                .filter(wine -> "red".equalsIgnoreCase(wine.getColor()))
                .count();

        long whiteWinesCount = wines.stream()
                .filter(wine -> "white".equalsIgnoreCase(wine.getColor())).count();
//      If need to return a list instead
//        List<WineDto> whiteWines = wines.stream()
//                .filter(wine -> "white".equalsIgnoreCase(wine.getColor()))
//                .toList();

        // Return a JSON object with two keys
        return Map.of("redCount", redWinesCount, "whiteCount", whiteWinesCount);
    }
}
