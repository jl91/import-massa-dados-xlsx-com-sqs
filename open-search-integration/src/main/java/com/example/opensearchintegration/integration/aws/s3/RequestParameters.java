package com.example.opensearchintegration.integration.aws.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RequestParameters {

    @JsonProperty("sourceIPAddress")
    private String sourceIPAddress;
}
