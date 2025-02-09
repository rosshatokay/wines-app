package com.example.wines_app.repository;

import com.example.wines_app.models.Wine;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WineRepository extends JpaRepository<Wine, Long> {
    @Query("SELECT w FROM Wine w WHERE " +
            "(CAST(:startDate AS date) IS NULL OR w.dateAdded >= :startDate) " +
            "AND (CAST(:endDate AS date) IS NULL OR w.dateAdded <= :endDate) " +
            "AND (:maxPh IS NULL OR w.pH <= :maxPh)")
    List<Wine> findByFilters(@Param("startDate") LocalDate startDate,
                             @Param("endDate") LocalDate endDate,
                             @Param("maxPh") Double maxPh);

    List<Wine> findByColorIgnoreCase(String color);

    // Relating the part 4, where we create a new table.
    // For instance, here we create a new table of high alcohol wines
    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE high_alcohol_wines AS SELECT * FROM wines WHERE alcohol > 12.5", nativeQuery = true)
    void createHighAlcoholTable();

    // Same, but a new table for low pH wines.
    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE low_ph_wines AS SELECT * FROM wines WHERE ph < 3.2", nativeQuery = true)
    void createLowPhTable();
}
