package com.ua.ezbir.services.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Getter
public class S3Service {
    private final AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.s3.url}")
    private String url;

    public S3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public void uploadFile(String keyName, MultipartFile file) throws IOException {
        s3client.putObject(bucketName, keyName, file.getInputStream(), null);
    }

    public S3Object getFile(String keyName) {
        return s3client.getObject(bucketName, keyName);
    }

    public void deleteFolder(String prefix) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(prefix);

        ObjectListing objectListing = s3client.listObjects(listObjectsRequest);

        while (true) {
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                s3client.deleteObject(bucketName, objectSummary.getKey());
            }
            if (objectListing.isTruncated()) {
                objectListing = s3client.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
    }
}