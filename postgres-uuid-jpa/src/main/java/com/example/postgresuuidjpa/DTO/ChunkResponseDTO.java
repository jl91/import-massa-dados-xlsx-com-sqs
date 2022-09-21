package com.example.postgresuuidjpa.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ChunkResponseDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("file_id")
    private UUID fileId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("first_line")
    private int firstLine;

    @JsonProperty("last_line")
    private int lastLine;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
