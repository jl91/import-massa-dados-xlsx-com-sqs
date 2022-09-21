package com.example.postgresuuidjpa.service;

import com.example.postgresuuidjpa.infrastructure.entities.ChunksProcessingEntity;
import com.example.postgresuuidjpa.infrastructure.entities.FileEntity;
import com.example.postgresuuidjpa.infrastructure.repositories.ChunksProcessingRepository;
import com.example.postgresuuidjpa.infrastructure.repositories.FileRepository;
import com.example.postgresuuidjpa.infrastructure.repositories.ProcessingStatusesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilesService {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    ChunksProcessingRepository chunksProcessingRepository;

    @Autowired
    ProcessingStatusesRepository processingStatusesRepository;

    public FileEntity saveFile(
            final String name
    ) {
        return fileRepository.save(new FileEntity()
                .setName(name)
        );
    }

    public List<ChunksProcessingEntity> saveChunksProcessing(
            final List<ChunksProcessingEntity> chunks
    ) {
        return (List<ChunksProcessingEntity>) chunksProcessingRepository.saveAll(chunks);
    }

    public Optional<FileEntity> fetchFileById(
            final UUID uuid
    ) {
        return fileRepository.findById(uuid);
    }

}
