package com.carbon.backend.repository;

import com.carbon.backend.model.CountryStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountryStatsRepository extends JpaRepository<CountryStats, Integer> {

    List<CountryStats> findByCountryOrderByEmissionYearAsc(String country);

    @Query("SELECT c FROM CountryStats c WHERE c.emissionYear = :year ORDER BY c.avgCo2 ASC")
    List<CountryStats> findRankingByEmissionYear(@Param("year") int year);

    @Query("SELECT DISTINCT c.country FROM CountryStats c ORDER BY c.country")
    List<String> findDistinctCountry();




}
