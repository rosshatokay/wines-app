package com.example.wines_app.repository;

import com.example.wines_app.models.Wine;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
