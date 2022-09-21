package com.example.postgresuuidjpa.infrastructure.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "file", schema = "public", catalog = "file_processing")
@Data
@Accessors(chain = true)
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Basic
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "fileByFileId")
    private Collection<ChunksProcessingEntity> chunksProcessingsById;

    @PrePersist
    void setDatetime() {
        createdAt = LocalDateTime.now();
    }
}
