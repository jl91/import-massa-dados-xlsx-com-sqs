package com.example.opensearchintegration.integration.aws.opensearch.documents;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public abstract class BaseDocument  implements Serializable {

    @JsonProperty("_id")
    private UUID uuid;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonProperty("update_at")
    private LocalDate updatedAt;
}
