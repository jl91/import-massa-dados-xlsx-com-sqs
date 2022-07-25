package com.example.api.integration.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class AWSS3Service {

    @Autowired
    AmazonS3 amazonS3;

    private static final String FILE_EXTENSION = "fileExtension";


    public boolean saveMultipartFileOnS3(
            final MultipartFile file,
            final String bucket
    ) throws IOException {
        initializeBucket(bucket);
        final var metadata = extractMetadata(file);
        final var fileName = file.getOriginalFilename();
        final var inputStream = file.getInputStream();

        final var putObjectResult = amazonS3.putObject(
                new PutObjectRequest(
                        bucket,
                        fileName,
                        inputStream,
                        metadata
                )
        );

        return putObjectResult.isRequesterCharged();
    }

    public S3Object getFileFromS3(
            final String bucket,
            final String filename
    ){
        return amazonS3.getObject(bucket, filename);
    }

    private void initializeBucket(
            final String bucketName
    ) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket(bucketName);
        }
    }

    private  ObjectMetadata extractMetadata(
            final MultipartFile file
    ) {
        final var metadata = new ObjectMetadata();

        final var originalFileName = file.getOriginalFilename();
        final var fileExtension = extractExtension(originalFileName);

        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        metadata.getUserMetadata().put(FILE_EXTENSION, fileExtension);

        return metadata;
    }

    private String extractExtension(final String file) {
        final var pieces = file.split("[\\.]");
        return pieces[pieces.length - 1];
    }

}
