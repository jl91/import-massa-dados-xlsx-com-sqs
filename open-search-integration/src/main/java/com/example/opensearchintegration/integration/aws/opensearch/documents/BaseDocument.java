package com.example.opensearchintegration.integration.aws.opensearch.documents;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public abstract class BaseDocument  implements Serializable {

//    @JsonProperty("_id")
    private String uuid;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonProperty("update_at")
    private LocalDate updatedAt;
}
