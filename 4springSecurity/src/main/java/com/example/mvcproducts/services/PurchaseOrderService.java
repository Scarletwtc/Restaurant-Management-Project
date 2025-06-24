package com.example.mvcproducts.services;

import com.example.mvcproducts.domain.PurchaseOrder;
import com.example.mvcproducts.repositories.PurchaseOrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PurchaseOrderService {

    private final PurchaseOrderRepository repo;

    public PurchaseOrderService(PurchaseOrderRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasAnyRole('SUPPLIER','OWNER')")
    @Transactional(readOnly = true)
    public List<PurchaseOrder> listAll() {
        return repo.findAll();
    }

    @PreAuthorize("hasRole('SUPPLIER')")
    public PurchaseOrder approve(Long id) {
        PurchaseOrder o = repo.findById(id).orElseThrow();
        o.updateStatus("APPROVED");
        return repo.save(o);
    }

    @PreAuthorize("hasRole('SUPPLIER')")
    public PurchaseOrder reject(Long id) {
        PurchaseOrder o = repo.findById(id).orElseThrow();
        o.updateStatus("REJECTED");
        return repo.save(o);
    }
}
