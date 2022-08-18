package com.example.opensearchintegration.integration.aws.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseElements {

    @JsonProperty("x-amz-request-id")
    private String xAmzRequestId;

    @JsonProperty("x-amz-id-2")
    private String xAmzId2;
}
