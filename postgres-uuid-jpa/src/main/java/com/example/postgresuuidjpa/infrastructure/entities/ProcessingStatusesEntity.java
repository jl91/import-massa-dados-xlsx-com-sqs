package com.example.postgresuuidjpa.infrastructure.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "processing_statuses", schema = "public", catalog = "file_processing")
@Data
@Accessors(chain = true)
public class ProcessingStatusesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private BigInteger id;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Basic
    @Column(name = "description", nullable = false, length = -1)
    private String description;

    @Basic
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "processingStatusesByProcessingStatusesId")
    private Collection<ChunksProcessingEntity> chunksProcessingsById;


    @PrePersist
    void setDatetime() {
        createdAt = LocalDateTime.now();
    }

}
