package com.example.api.integration.sqs.publishers.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewFileUploaded  implements Serializable {

    @JsonProperty("bucket")
    private String bucket;

    @JsonProperty("fileName")
    private String fileName;
}
