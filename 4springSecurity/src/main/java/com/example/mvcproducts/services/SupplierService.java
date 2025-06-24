package com.example.mvcproducts.services;

import com.example.mvcproducts.domain.Supplier;
import com.example.mvcproducts.repositories.SupplierRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository repo;

    public SupplierService(SupplierRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasRole('OWNER')")
    public Supplier create(Supplier s) {
        return repo.save(s);
    }

    @PreAuthorize("hasAnyRole('SUPPLIER','OWNER')")
    @Transactional(readOnly = true)
    public List<Supplier> listAll() {
        return repo.findAll();
    }

    @PreAuthorize("hasAnyRole('SUPPLIER','OWNER')")
    public String performance(Long id) {
        return repo.findById(id)
                .map(Supplier::getPerformanceMetrics)
                .orElse("Unknown supplier");
    }
}
