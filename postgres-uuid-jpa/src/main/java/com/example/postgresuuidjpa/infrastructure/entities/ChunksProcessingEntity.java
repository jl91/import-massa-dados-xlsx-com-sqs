package com.example.postgresuuidjpa.infrastructure.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chunks_processing", schema = "public", catalog = "file_processing")
@IdClass(ChunksProcessingEntityPK.class)
@Data
@Accessors(chain = true)
public class ChunksProcessingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Id
    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @Basic
    @Column(name = "processing_statuses_id", nullable = false)
    private int processingStatusesId;

    @Basic
    @Column(name = "first_line", nullable = false)
    private int firstLine;

    @Basic
    @Column(name = "last_line", nullable = false)
    private int lastLine;

    @Basic
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Basic
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "file_id",
            referencedColumnName = "id",
            nullable = false,
            updatable = false,
            insertable = false
    )
    private FileEntity fileByFileId;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "processing_statuses_id",
            referencedColumnName = "id",
            nullable = false,
            updatable = false,
            insertable = false
    )
    private ProcessingStatusesEntity processingStatusesByProcessingStatusesId;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
