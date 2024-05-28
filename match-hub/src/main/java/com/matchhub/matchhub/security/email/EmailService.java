package com.matchhub.matchhub.security.email;

import com.google.api.services.gmail.Gmail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final GmailService gmailService;

    @Value("${gmail.api.from}")
    private String FROM_GMAIL_API;

    @Value("${gmail.api.app.link}")
    private String LINK_APP_RESET_PASSWORD;

    public void sendRecoveryEmail(String emailHubUser, String token) throws IOException, MessagingException, GeneralSecurityException {
        // Create a service that will manage the email sending
        Gmail service = gmailService.gmailServiceFactory();

        // Set up email information
        String user = "me";
        String to = emailHubUser;
        String from = FROM_GMAIL_API;
        String subject = "[MatchHub] Password Reset Request - Please Do Not Reply";
        String bodyText = getBodyText(token);

        // Create email object
        MimeMessage emailContent = createEmail(to, from, subject, bodyText);
        // Send the email
        gmailService.sendMessage(service, user, emailContent);
    }

    private String getBodyText(String token) {
        String resetLink = "https://" + LINK_APP_RESET_PASSWORD + "?token=" + token;
        String bodyText = "Hello," +
                "\n\nYou have requested to reset your password for your MatchHub account. " +
                "Please click on the link below to set a new password. " +
                "This link will expire in 15 minutes for security reasons.\n\n" +
                resetLink +
                "\n\nIf you did not request a password reset, " +
                "please ignore this email or contact support if you have any concerns." +
                "\n\nThank you,\nMatchHub Team";
        return bodyText;
    }

    // MimeMessage: constructs and configures details of an email (class from the Java Mail API)
    public static MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException, UnsupportedEncodingException {
        // Properties: used to configure parameters for the email session
        Properties props = new Properties();
        // Session: represents a mail session, grouping all settings and
        // necessary properties for sending emails, such as
        // SMTP server information, authentication, etc. (class from the Java Mail API)
        // getDefaultInstance: obtains an instance of Session with these properties or creates one if it does not exist
        // null: provides credentials when the email server requires authentication
        // Passing `null` means that no custom authentication is being provided
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session); // Create a MimeMessage using the created session
        email.setFrom(new InternetAddress(from, "Matchub")); // Set the sender's email address.
        email.addRecipient(// Add recipient
                javax.mail.Message.RecipientType.TO, // Indicates that the recipient is the primary one
                new InternetAddress(to)); // Set the recipient's email address.
        email.setSubject(subject); // Set the email's subject.
        email.setText(bodyText); // Set the text of the email body.
        return email; // Return the created email
    }

}
