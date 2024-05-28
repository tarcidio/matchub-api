package com.matchhub.matchhub.security.email;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GmailService {
    // JSON Factory instance used for processing (serializing and deserializing) JSON data
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // Permission scopes required by your application to access the Gmail API.
    // In this case, `GmailScopes.GMAIL_SEND` allows the application to send emails on behalf of the authenticated user
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);

    // Path to the directory where OAuth2 authentication tokens are stored locally
    @Value("${gmail.api.tokens}")
    private String TOKENS_DIRECTORY_PATH;

    // Path to the JSON file containing Google API credentials
    @Value("${gmail.api.credentials}")
    private String CREDENTIALS_FILE_PATH;

    // Constant storing the application name used when configuring the Gmail API client.
    // This name is generally used for logging and monitoring on Google Cloud Platform.
    @Value("${gmail.api.app.name}")
    private String APPLICATION_NAME;

    public Gmail gmailServiceFactory() throws IOException, GeneralSecurityException {
        // NetHttpTransport: class used to configure and execute low-level HTTP requests
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Gmail: class used to perform operations supported by the Gmail API such as sending emails, managing messages, etc.
        return new Gmail.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                getCredentials(HTTP_TRANSPORT)
        ).setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Open the credentials file
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);
        FileInputStream credentialsStream = new FileInputStream(credentialsFile);

        // GoogleClientSecrets: encapsulates client credentials
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(credentialsStream));

        // GoogleAuthorizationCodeFlow: manages OAuth 2.0 authorization flow for applications accessing protected Google APIs
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        // LocalServerReceiver: creates a temporary local HTTP server used to listen for Google's response after user authorization
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .setHost("localhost")
                .setCallbackPath("/oauth2callback")
                .build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    // Message: represents an email message (class from the Gmail API)
    public Message sendMessage(Gmail service, String userId, MimeMessage emailContent) throws MessagingException, IOException {
        // ByteArrayOutputStream: stores data in memory
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);

        // Convert accumulated data into a byte array
        byte[] bytes = buffer.toByteArray();
        // Base64: provides methods for encoding and decoding data using Base64 encoding
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);

        Message message = new Message(); // Create a Message object to be used for sending the email
        message.setRaw(encodedEmail); // Set the encoded email content

        // Send the email
        message = service.users().messages().send(userId, message).execute();
        return message;
    }
}