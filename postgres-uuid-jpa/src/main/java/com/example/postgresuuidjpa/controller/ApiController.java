package com.example.postgresuuidjpa.controller;

import com.example.postgresuuidjpa.DTO.NewFileRequestDTO;
import com.example.postgresuuidjpa.infrastructure.entities.ChunksProcessingEntity;
import com.example.postgresuuidjpa.infrastructure.entities.FileEntity;
import com.example.postgresuuidjpa.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


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

        for (int i = 0; i < 1; i++) {
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


}
