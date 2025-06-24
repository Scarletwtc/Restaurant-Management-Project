package com.example.mvcproducts.repositories;

import com.example.mvcproducts.domain.WasteReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasteReportRepository extends JpaRepository<WasteReport, Long> {
    List<WasteReport> findTop5ByOrderByDateDesc();
} 