package com.example.postgresuuidjpa.infrastructure.repositories;

import com.example.postgresuuidjpa.infrastructure.entities.FileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileRepository extends CrudRepository<FileEntity, UUID> {
}
