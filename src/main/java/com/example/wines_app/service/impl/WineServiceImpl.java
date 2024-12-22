package com.example.wines_app.service.impl;

import com.example.wines_app.dto.WineDto;
import com.example.wines_app.models.Wine;
import com.example.wines_app.repository.WineRepository;
import com.example.wines_app.service.WineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.lang.reflect.Field;


@Service

public class WineServiceImpl implements WineService {
    @Autowired
    private ModelMapper modelMapper;
    private WineRepository wineRepository;

    @Autowired
    private WineServiceImpl(WineRepository wineRepository) {
        this.wineRepository = wineRepository;
    }

    @Override
    public List<WineDto> findAllWines() {
        List<Wine> wines = wineRepository.findAll();
        return wines.stream().map(this::mapToWineDto).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> findWines(String color, List<String> fields) {
        List<Wine> wines;

        // Filter by color if specified
        if (color == null || color.isBlank()) {
            wines = wineRepository.findAll();
        } else {
            wines = wineRepository.findByColorIgnoreCase(color);
        }

        // Map and project fields dynamically
        return wines.stream().map(wine -> projectFields(wine, fields)).toList();
    }

    // Helper method to project specific fields
    private Map<String, Object> projectFields(Wine wine, List<String> fields) {
        Map<String, Object> projectedFields = new HashMap<>();

        // Use reflection to dynamically access fields
        for (String field : fields) {
            try {
                Field declaredField = Wine.class.getDeclaredField(field);
                declaredField.setAccessible(true);
                projectedFields.put(field, declaredField.get(wine));
            } catch (NoSuchFieldException | IllegalAccessError | IllegalAccessException e) {
                System.err.println("Invalid field: " + field);
            }
        }

        return projectedFields;
    }

    private WineDto mapToWineDto(Wine wine) {
        WineDto wineDto = WineDto.builder()
                .id(wine.getId())
                .fixedAcidity(wine.getFixedAcidity())
                .chlorides(wine.getChlorides())
                .volatileAcidity(wine.getVolatileAcidity())
                .citricAcid(wine.getCitricAcid())
                .residualSugar(wine.getResidualSugar())
                .freeSulfurDioxide(wine.getFreeSulfurDioxide())
                .totalSulfurDioxide(wine.getTotalSulfurDioxide())
                .density(wine.getDensity())
                .pH(wine.getPH())
                .sulphates(wine.getSulphates())
                .alcohol(wine.getAlcohol())
                .color(wine.getColor())
                .quality(wine.getQuality())
            .build();

        return wineDto;
    }
}
