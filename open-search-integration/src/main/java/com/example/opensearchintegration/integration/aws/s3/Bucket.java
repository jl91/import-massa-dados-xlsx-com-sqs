package com.example.opensearchintegration.integration.aws.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Bucket {

    @JsonProperty("name")
    private String name;

    @JsonProperty("ownerIdentity")
    private OwnerIdentity ownerIdentity;

    @JsonProperty("arn")
    private String arn;

}
