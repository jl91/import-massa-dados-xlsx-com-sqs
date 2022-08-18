package com.example.opensearchintegration.integration.aws.opensearch.repositories;

import com.example.opensearchintegration.integration.aws.opensearch.entities.LineEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends PagingAndSortingRepository<LineEntity, String> {}
