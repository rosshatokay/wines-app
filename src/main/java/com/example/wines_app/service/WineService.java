package com.example.wines_app.service;

import com.example.wines_app.dto.WineDto;

import java.util.List;

public interface WineService {
    List<WineDto> findAllWines();
    List<WineDto> findWinesByColor(String color);
}
