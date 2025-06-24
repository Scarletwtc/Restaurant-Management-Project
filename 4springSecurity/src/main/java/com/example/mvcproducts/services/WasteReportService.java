package com.example.mvcproducts.services;

import com.example.mvcproducts.domain.WasteReport;
import com.example.mvcproducts.repositories.WasteReportRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WasteReportService {

    private final WasteReportRepository repo;

    public WasteReportService(WasteReportRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasRole('CHEF')")
    public WasteReport create(WasteReport r) {
        return repo.save(r);
    }

    @PreAuthorize("hasAnyRole('CHEF','OWNER')")
    @Transactional(readOnly = true)
    public List<WasteReport> recent() {
        return repo.findTop5ByOrderByDateDesc();
    }

    @PreAuthorize("hasRole('CHEF')")
    public WasteReport update(Long id, Double qty, String reason) {
        WasteReport r = repo.findById(id).orElseThrow();
        r.updateWasteData(qty, reason);
        return repo.save(r);
    }
}
