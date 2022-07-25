package com.example.api.integration.database.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Data
public class CompanyEntity {

    @Id
    @Column(name = "document_number", nullable = false, length = 14)
    private String id;

    @Column(name = "name", length = 120)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "disabled_at")
    private LocalDateTime disabledAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    private void prePersist() {
        final LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.setCreatedAt(now);
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }

}