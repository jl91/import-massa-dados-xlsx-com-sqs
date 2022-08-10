package com.example.elasticsearchintegration.integration.aws.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ObjectRecord {

    @JsonProperty("eventVersion")
    private String eventVersion;

    @JsonProperty("eventSource")
    private String eventSource;

    @JsonProperty("awsRegion")
    private String awsRegion;

    @JsonProperty("eventTime")
    private String eventTime;

    @JsonProperty("eventName")
    private String eventName;

    @JsonProperty("userIdentity")
    private UserIdentity userIdentity;

    @JsonProperty("requestParameters")
    private RequestParameters requestParameters;

    @JsonProperty("responseElements")
    private ResponseElements responseElements;

    @JsonProperty("s3")
    private S3 s3;

}
