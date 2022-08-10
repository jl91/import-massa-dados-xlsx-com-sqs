package com.example.elasticsearchintegration.integration.aws.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class S3 {

    @JsonProperty("s3SchemaVersion")
    private String s3SchemaVersion;

    @JsonProperty("configurationId")
    private String configurationId;

    @JsonProperty("bucket")
    private Bucket bucket;

    @JsonProperty("object")
    private Object object;

}
