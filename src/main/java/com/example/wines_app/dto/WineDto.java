package com.example.wines_app.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder

public class WineDto {
    private long id;

    private Double fixedAcidity;

    private Double volatileAcidity;

    private Double citricAcid;

    private Double residualSugar;

    private Double chlorides;

    private Integer freeSulfurDioxide;

    private Integer totalSulfurDioxide;

    private Double density;

    private Double pH;

    private Double sulphates;

    private Double alcohol;

    private String quality;

    private String color;

    private LocalDate date_added;
}
