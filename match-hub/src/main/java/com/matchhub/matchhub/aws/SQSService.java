package com.matchhub.matchhub.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class SQSService{

    @Value("${aws.sqs.evaluation.notification.url}")
    private String queueUrl;

    @Value("${aws.region}")
    private String awsSQSRegion;

    private SqsClient createSQSClient() {
        return SqsClient.builder()
                .region(Region.of(awsSQSRegion)) // Ajuste a região conforme necessário
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public void sendNotificationToSQS(String messageBody) {
        // Criação do cliente dentro do try-with-resources para garantir o fechamento automático
        try (SqsClient sqsClient = createSQSClient()) {
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build();

            SendMessageResponse response = sqsClient.sendMessage(sendMsgRequest);
        } // O sqsClient será automaticamente fechado aqui, mesmo se uma exceção for lançada
    }


}
