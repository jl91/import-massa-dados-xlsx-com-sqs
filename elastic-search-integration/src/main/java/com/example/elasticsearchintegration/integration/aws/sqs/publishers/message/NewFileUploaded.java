package com.example.elasticsearchintegration.integration.aws.sqs.publishers.message;

import com.example.elasticsearchintegration.integration.aws.s3.ObjectUploaded;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewFileUploaded extends ObjectUploaded {
}
