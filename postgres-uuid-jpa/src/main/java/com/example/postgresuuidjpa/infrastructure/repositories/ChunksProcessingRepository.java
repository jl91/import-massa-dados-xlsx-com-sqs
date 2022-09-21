package com.example.postgresuuidjpa.infrastructure.repositories;

import com.example.postgresuuidjpa.infrastructure.entities.ChunksProcessingEntity;
import com.example.postgresuuidjpa.infrastructure.entities.ChunksProcessingEntityPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunksProcessingRepository extends CrudRepository<ChunksProcessingEntity, ChunksProcessingEntityPK> {
}
