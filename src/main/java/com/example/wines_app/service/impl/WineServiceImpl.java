package com.example.wines_app.service.impl;

import com.example.wines_app.dto.WineDto;
import com.example.wines_app.models.Wine;
import com.example.wines_app.repository.WineRepository;
import com.example.wines_app.service.WineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class WineServiceImpl implements WineService {
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

    private WineDto mapToWineDto(Wine wine) {
        WineDto wineDto = WineDto.builder()
                .id(wine.getId())
                .fixedAcidity(wine.getFixedAcidity())
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
