package com.example.elasticsearchintegration.integration.aws.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserIdentity {

    @JsonProperty("principalId")
    private String principalId;
}
