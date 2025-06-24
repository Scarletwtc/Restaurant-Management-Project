package com.example.mvcproducts.services;

import com.example.mvcproducts.domain.KitchenOrder;
import com.example.mvcproducts.repositories.KitchenOrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class KitchenOrderService {

    private final KitchenOrderRepository repo;

    public KitchenOrderService(KitchenOrderRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasRole('CHEF')")
    public KitchenOrder placeOrder(KitchenOrder o) {
        o.setOrderTime(new Date());
        o.setStatus("NEW");
        return repo.save(o);
    }

    @PreAuthorize("hasAnyRole('CHEF','OWNER')")
    @Transactional(readOnly = true)
    public List<KitchenOrder> listByStatus(String status) {
        return repo.findByStatus(status);
    }

    @PreAuthorize("hasRole('CHEF')")
    public KitchenOrder updateStatus(Long id, String status) {
        KitchenOrder o = repo.findById(id).orElseThrow();
        o.updateStatus(status);
        return repo.save(o);
    }
}
