package com.example.wines_app.service;

import com.example.wines_app.dto.WineDto;

import java.util.List;
import java.util.Map;

public interface WineService {
    List<WineDto> findAllWines();
    List<Map<String, Object>> findWines(String color, List<String> fields);
}
