package com.example.wines_app.service.impl;

import com.example.wines_app.dto.WineDto;
import com.example.wines_app.models.Wine;
import com.example.wines_app.repository.WineRepository;
import com.example.wines_app.service.CsvImportService;
import com.example.wines_app.service.WineService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.lang.reflect.Field;


@Service

public class WineServiceImpl implements WineService {
    @Autowired
    private final WineRepository wineRepository;
    private final CsvImportService csvImportService;
    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.username}")
    private String username; // db username

    @Value("${spring.datasource.password}")
    private String password; // db password

    @Autowired
    public WineServiceImpl(WineRepository wineRepository, JdbcTemplate jdbcTemplate, CsvImportService csvImportService) {
        this.wineRepository = wineRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.csvImportService = csvImportService;
    }

    // method to create a separate database and tables
    @Override
    public void createNewDatabaseAndTables() {
        String newDbName = "wines_analysis_db";

        // Step 1: Check if the database already exists
        String checkDbExistsQuery = "SELECT 1 FROM pg_database WHERE datname = ?";
        List<Integer> exists = jdbcTemplate.queryForList(checkDbExistsQuery, Integer.class, newDbName);

        // Create a new database if it doesn't exist
        if (!exists.isEmpty()) {
            System.out.println("Database already exists: " + newDbName);
            return;
        } else {
            jdbcTemplate.execute("CREATE DATABASE " + newDbName);
        }

        // Step 3: Create tables inside a transaction
        createTablesInNewDb(newDbName);
    }

    @Transactional
    public void createTablesInNewDb(String newDbName) {
        JdbcTemplate newDbJdbcTemplate = new JdbcTemplate(getNewDataSource(newDbName));

        // Step 4: Create `wines` table in the new database
        String createWinesTableQuery = """
        CREATE TABLE IF NOT EXISTS wines (
            id SERIAL PRIMARY KEY,
            name VARCHAR(255),
            alcohol DOUBLE PRECISION,
            ph DOUBLE PRECISION,
            date_added DATE
        )
    """;
        newDbJdbcTemplate.execute(createWinesTableQuery);

        // Step 5: Copy data from original database
        List<Map<String, Object>> originalWines = jdbcTemplate.queryForList("SELECT * FROM wines"); // Fetch all wines

        for (Map<String, Object> row : originalWines) {
            newDbJdbcTemplate.update("INSERT INTO wines (id, name, alcohol, ph, date_added) " +
                            "VALUES (?, ?, ?, ?, ?) " +
                            "ON CONFLICT (id) DO NOTHING",
                    row.get("id"),
                    row.get("name"),
                    row.get("alcohol"),
                    row.get("ph"),
                    row.get("date_added")
            );
        }

        System.out.println("Data copied successfully to new database!");

        // Step 6: Now create filtered tables using `SELECT`
        newDbJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS high_alcohol_wines AS SELECT * FROM wines WHERE alcohol > 12.5");
        newDbJdbcTemplate.execute("CREATE TABLE IF NOT EXISTS low_ph_wines AS SELECT * FROM wines WHERE ph < 3.2");

        System.out.println("New database and tables created successfully!");
    }

    private DataSource getNewDataSource(String dbName) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/" + dbName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Override
    public List<WineDto> getWinesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Wine> wines = wineRepository.findByFilters(startDate, endDate, null);

        return wines.stream().map(this::mapToWineDto).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> findWines(String color, List<String> fields, String start_date, String end_date, Double maxPh) {
        List<Wine> wines;


        // Filter by color if specified
        if (color == null || color.isBlank()) {
            wines = wineRepository.findByFilters(parseDate(start_date), parseDate(end_date), maxPh);
        } else {
            wines = wineRepository.findByColorIgnoreCase(color);
        }

        // Map and project fields dynamically
        return wines.stream().map(wine -> projectFields(wine, fields)).toList();
    }

    private LocalDate parseDate(String date)
    {
        return (date != null && !date.isBlank()) ? LocalDate.parse(date) : null;
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
                .date_added(wine.getDateAdded())
            .build();

        return wineDto;
    }
}