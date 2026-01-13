package com.example.bom.audit;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin
public class AuditController {

    private final AuditLogRepository repository;

    public AuditController(AuditLogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/part/{partId}")
    public List<AuditLog> getAuditLogs(@PathVariable Long partId) {
        return repository.findByEntityTypeAndEntityIdOrderByTimestampDesc(
                "PART",
                partId
        );
    }
}
