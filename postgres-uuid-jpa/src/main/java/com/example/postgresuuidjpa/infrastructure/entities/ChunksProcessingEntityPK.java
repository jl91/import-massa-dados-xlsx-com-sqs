package com.example.postgresuuidjpa.infrastructure.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ChunksProcessingEntityPK implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Id
    @Column(name = "file_id", nullable = false)
    private UUID fileId;

}
