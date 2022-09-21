package com.example.postgresuuidjpa.controller;

import com.example.postgresuuidjpa.DTO.ChunkResponseDTO;
import com.example.postgresuuidjpa.DTO.NewFileRequestDTO;
import com.example.postgresuuidjpa.infrastructure.entities.ChunksProcessingEntity;
import com.example.postgresuuidjpa.infrastructure.entities.FileEntity;
import com.example.postgresuuidjpa.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api")
public class ApiController {

    @Autowired
    FilesService filesService;

    @PostMapping("files")
    public FileEntity newFile(
            @RequestBody final NewFileRequestDTO newFileRequestDTO
    ) {
        return filesService.saveFile(newFileRequestDTO.getFilename());
    }

    @GetMapping("files/{fileId}")
    public Optional<FileEntity> fetchAll(
            @PathVariable("fileId") final UUID fileId
    ) {
        return filesService.fetchFileById(fileId);
    }

    @PostMapping("files/{fileId}/chunks")
    public void newFileChunk(
            @PathVariable("fileId") final UUID fileId
    ) {

        final var chunks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            chunks.add(
                    new ChunksProcessingEntity()
                            .setFileId(fileId)
                            .setFirstLine(i)
                            .setLastLine(i + 10)
                            .setProcessingStatusesId(1)
            );
        }

        filesService.saveChunksProcessing((List) chunks);
    }

    @GetMapping("files/{fileId}/chunks")
    public List<ChunkResponseDTO> fetchAllChunks(
            @PathVariable("fileId") final UUID fileId
    ) {

        final var files = filesService.fetchFileById(fileId);

        if (files.isEmpty()) {
            return List.of();
        }

        return files.get().getChunksProcessingsById()
                .stream()
                .map(entity -> new ChunkResponseDTO()
                        .setId(entity.getId())
                        .setFileId(entity.getFileId())
                        .setStatus(entity.getProcessingStatusesByProcessingStatusesId().getName())
                        .setFirstLine(entity.getFirstLine())
                        .setLastLine(entity.getLastLine())
                        .setCreatedAt(entity.getCreatedAt())
                        .setUpdatedAt(entity.getUpdatedAt())
                )
                .collect(Collectors.toList());
    }


}
