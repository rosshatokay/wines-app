package com.example.wines_app.service;

import com.example.wines_app.dto.WineDto;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WineService {
    List<WineDto> getWinesByDateRange(LocalDate startDate, LocalDate endDate);
    List<Map<String, Object>> findWines(String color, List<String> fields, String start_date, String end_date, Double maxPh);

    @Transactional
    void createNewDatabaseAndTables();
}
