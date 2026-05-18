package com.carbon.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "country_stats")
@Builder @NoArgsConstructor
@AllArgsConstructor
public class CountryStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private Integer emissionYear;

    private Double avgCo2;
    private Double maxCo2;
    private Double minCo2;
    private Integer recordCount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
