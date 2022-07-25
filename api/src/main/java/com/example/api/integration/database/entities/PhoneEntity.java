package com.example.api.integration.database.entities;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "phones")
@Data
@Accessors(chain = true)
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE customers set deleted_at = now() where document_number = ?", check = ResultCheckStyle.COUNT)
public class PhoneEntity {

    @EmbeddedId
    private PhoneId id;

    @MapsId("customerDocumentNumber")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_document_number", nullable = false)
    private CustomerEntity customerEntityDocumentNumber;

    @Column(name = "ddd", nullable = false)
    private Integer ddd;

    @Column(name = "phone", nullable = false)
    private Integer phone;

    @Column(name = "is_checked", nullable = false)
    private Boolean isChecked = false;

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