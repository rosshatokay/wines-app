package com.example.wines_app.service;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CsvImportService {
    private final JdbcTemplate jdbcTemplate;

    public CsvImportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void loadCsvData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("data/wines.csv").getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Match CSV format

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip the header row
                }
                String[] data = line.split(",");

                // Ensure correct number of columns before insertion
                if (data.length != 15) {
                    System.err.println("❌ Skipping row due to incorrect column count: " + line);
                    continue;
                }

                try {
                    Integer id = parseInteger(data[0]); // Parse ID safely
                    Double fixedAcidity = parseDouble(data[1]);
                    Double volatileAcidity = parseDouble(data[2]);
                    Double citricAcid = parseDouble(data[3]);
                    Double residualSugar = parseDouble(data[4]);
                    Double chlorides = parseDouble(data[5]);
                    Double freeSulfurDioxide = parseDouble(data[6]);
                    Double totalSulfurDioxide = parseDouble(data[7]);
                    Double density = parseDouble(data[8]);
                    Double ph = parseDouble(data[9]);
                    Double sulphates = parseDouble(data[10]);
                    Double alcohol = parseDouble(data[11]);
                    String color = data[12].trim();
                    String quality = data[13].trim();
                    LocalDate dateAdded = LocalDate.parse(data[14].trim(), dateFormatter); // Convert to LocalDate

                    jdbcTemplate.update("INSERT INTO wines (id, fixed_acidity, volatile_acidity, citric_acid, " +
                                    "residual_sugar, chlorides, free_sulfur_dioxide, total_sulfur_dioxide, density, " +
                                    "ph, sulphates, alcohol, color, quality, date_added) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                                    + "ON CONFLICT (id) DO NOTHING",
                            id, fixedAcidity, volatileAcidity, citricAcid, residualSugar, chlorides,
                            freeSulfurDioxide, totalSulfurDioxide, density, ph, sulphates, alcohol,
                            color, quality, dateAdded);
                } catch (Exception e) {
                    System.err.println("❌ Skipping row due to error: " + line);
                    e.printStackTrace();
                }
            }
            System.out.println("✅ CSV Data Imported Successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error importing CSV: " + e.getMessage());
        }
    }

    // Helper method to safely parse Integer values
    private Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return null; // If conversion fails, return NULL
        }
    }

    // Helper method to safely parse Double values
    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return null; // If conversion fails, return NULL
        }
    }
}
