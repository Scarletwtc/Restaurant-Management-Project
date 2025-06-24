package com.example.mvcproducts.services;

import com.example.mvcproducts.domain.ExpiryAlert;
import com.example.mvcproducts.repositories.ExpiryAlertRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ExpiryAlertService {

    private final ExpiryAlertRepository repo;

    public ExpiryAlertService(ExpiryAlertRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasAnyRole('CHEF','OWNER')")
    @Transactional(readOnly = true)
    public List<ExpiryAlert> listOpenAlerts() {
        return repo.findByStatus("TRIGGERED");
    }

    @PreAuthorize("hasAnyRole('CHEF','OWNER')")
    @Transactional(readOnly = true)
    public List<ExpiryAlert> findByDateBefore(Date date) {
        return repo.findByAlertDateBefore(date);
    }

    @PreAuthorize("hasAnyRole('CHEF','OWNER')")
    public ExpiryAlert resolve(Long id) {
        ExpiryAlert a = repo.findById(id).orElseThrow();
        a.resolveAlert();
        return repo.save(a);
    }
}
