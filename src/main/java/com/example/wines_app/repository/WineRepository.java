package com.example.wines_app.repository;

import com.example.wines_app.models.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
    
public interface WineRepository extends JpaRepository<Wine, Long> {
    List<Wine> findAll();
    List<Wine> findByColorIgnoreCase(String color);
}
