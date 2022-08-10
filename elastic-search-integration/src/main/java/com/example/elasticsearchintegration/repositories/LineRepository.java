package com.example.elasticsearchintegration.repositories;

import com.example.elasticsearchintegration.entities.LineEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends PagingAndSortingRepository<LineEntity, String> {}
