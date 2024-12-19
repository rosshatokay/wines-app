package com.example.wines_app.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.aot.generate.GeneratedTypeReference;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wines")

public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fixed_acidity", nullable = false)
    private Double fixedAcidity;

    @Column(name = "volatile_acidity", nullable = false)
    private Double volatileAcidity;

    @Column(name = "citric_acid", nullable = false)
    private Double citricAcid;

    @Column(name = "residual_sugar", nullable = false)
    private Double residualSugar;

    @Column(name = "chlorides", nullable = false)
    private Double chlorides;

    @Column(name = "free_sulfur_dioxide", nullable = false)
    private Integer freeSulfurDioxide;

    @Column(name = "total_sulfur_dioxide", nullable = false)
    private Integer totalSulfurDioxide;

    @Column(name = "density", nullable = false)
    private Double density;

    @Column(name = "pH", nullable = false)
    private Double pH;

    @Column(name = "sulphates", nullable = false)
    private Double sulphates;

    @Column(name = "alcohol", nullable = false)
    private Double alcohol;

    @Column(name = "quality", nullable = false)
    private String quality;

    @Column(name = "color", nullable = false)
    private String color;
}
