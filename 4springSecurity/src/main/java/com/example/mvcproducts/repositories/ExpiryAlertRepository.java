package com.example.mvcproducts.repositories;

import com.example.mvcproducts.domain.ExpiryAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExpiryAlertRepository extends JpaRepository<ExpiryAlert, Long> {
    List<ExpiryAlert> findByStatus(String status);
    List<ExpiryAlert> findByAlertDateBefore(Date date);
}
