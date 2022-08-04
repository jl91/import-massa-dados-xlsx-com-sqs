package com.example.elasticcacheintegration.repositories;

import com.example.elasticcacheintegration.entities.LineEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends PagingAndSortingRepository<LineEntity, String> {}
