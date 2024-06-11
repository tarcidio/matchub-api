package com.matchhub.matchhub.aws;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class S3Service{
    @Value("${aws.s3.bucket.hubuser.images}")
    private String bucketName;

    @Value("${aws.region}")
    private String awsS3Region;

    private S3Client createS3Client() {
        return S3Client.builder()
                .region(Region.of(awsS3Region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    private boolean isValidFileName(String fileName) {
        // Verifica se o nome do arquivo contém sequências que podem levar a path traversal
        return !fileName.contains("..");
    }

    public String uploadImage(String fileName, File file) {
        if (!isValidFileName(fileName)) {
            throw new IllegalArgumentException("Invalid file name detected!");
        }

        S3Client s3Client = createS3Client();
        PutObjectResponse response = s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                RequestBody.fromFile(file)
        );

        file.delete(); // To clean up the temporary file

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
    }
}