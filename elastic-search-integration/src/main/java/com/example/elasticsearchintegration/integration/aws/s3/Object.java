package com.example.elasticsearchintegration.integration.aws.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Object {

    @JsonProperty("key")
    private String key;

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("eTag")
    private String eTag;

    @JsonProperty("sequencer")
    private String sequencer;
}
