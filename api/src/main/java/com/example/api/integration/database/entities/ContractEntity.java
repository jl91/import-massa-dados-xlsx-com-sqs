package com.example.api.integration.database.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@Data
@Accessors(chain = true)
@Where(clause = "deleted_at is null")
//@SQLDelete(sql = "UPDATE contracts set deleted_at = now() where id = ?", check = ResultCheckStyle.COUNT)
public class ContractEntity {

    @EmbeddedId
    private ContractId id;

    @MapsId("customerDocumentNumber")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_document_number", nullable = false)
    private CustomerEntity customerEntityDocumentNumber;

    @MapsId("companyEntityDocumentNumber")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_document_number", nullable = false)
    private CompanyEntity companyEntityDocumentNumber;

    @Column(name = "value", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "installments_size", nullable = false)
    private Integer installmentsSize;

    @Lob
    @Column(name = "warning_level", nullable = false)
    private String warningLevel;

    @Column(name = "reference_date", nullable = false)
    private LocalDate referenceDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "disabled_at")
    private LocalDateTime disabledAt;

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