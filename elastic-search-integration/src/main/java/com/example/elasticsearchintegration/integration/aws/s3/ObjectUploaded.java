package com.example.elasticsearchintegration.integration.aws.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ObjectUploaded {

    @JsonProperty("Records")
    private List<ObjectRecord> records;
}
