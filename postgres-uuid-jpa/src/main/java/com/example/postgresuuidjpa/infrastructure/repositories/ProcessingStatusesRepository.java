package com.example.postgresuuidjpa.infrastructure.repositories;

import com.example.postgresuuidjpa.infrastructure.entities.ProcessingStatusesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ProcessingStatusesRepository extends CrudRepository<ProcessingStatusesEntity, BigInteger> {
}
