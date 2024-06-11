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
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final GmailService gmailService;

    @Value("${gmail.api.from}")
    private String FROM_GMAIL_API;

    @Value("${app.link.reset}")
    private String LINK_APP_RESET_PASSWORD;

    @Value("${app.link.check}")
    private String LINK_APP_CHECK_EMAIL;

    private void sendAuthEmail(String emailHubUser, String subjectEmail, String token,
                               Function<String, String> getBodyText)
            throws GeneralSecurityException, IOException, MessagingException {
        // Create a service that will manage the email sending
        Gmail service = gmailService.gmailServiceFactory();

        // Create email object
        MimeMessage emailContent = createEmail(
                emailHubUser,
                FROM_GMAIL_API,
                subjectEmail,
                getBodyText.apply(token)
        );

        // Send the email
        gmailService.sendMessage(
                service,
                "me",
                emailContent
        );
    }

    public void sendCheckEmail(String emailHubUser, String token) throws GeneralSecurityException, IOException, MessagingException {
        // Set up email information
        sendAuthEmail(
                emailHubUser,
                "[MatcHub] Email Verification Request - Please Do Not Reply",
                token,
                getBodyTextCheckEmail
        );
    }

    public void sendRecoveryEmail(String emailHubUser, String token) throws IOException, MessagingException, GeneralSecurityException {
        // Set up email information
        sendAuthEmail(
                emailHubUser,
                "[MatcHub] Password Reset Request - Please Do Not Reply",
                token,
                getBodyTextResetPassword
        );
    }

    private final Function<String, String> getBodyTextCheckEmail = token -> {
        String link = LINK_APP_CHECK_EMAIL + token;
        return "Hello," +
                "\n\nThank you for registering with MatcHub. " +
                "To complete your registration and verify your email address, " +
                "please click on the link below. " +
                "This link will expire in 24 hours for security reasons.\n\n" +
                link +
                "\n\nIf you did not create an account, no further action is required. " +
                "However, if you feel this is an error, please contact our support team.\n\n" +
                "Thank you,\n" +
                "MatcHub Team";
    };

    private final Function<String, String> getBodyTextResetPassword = token -> {
        String link = LINK_APP_RESET_PASSWORD + token;
        return "Hello," +
                "\n\nYou have requested to reset your password for your MatcHub account. " +
                "Please click on the link below to set a new password. " +
                "This link will expire in 15 minutes for security reasons.\n\n" +
                link +
                "\n\nIf you did not request a password reset, " +
                "please ignore this email or contact support if you have any concerns." +
                "\n\nThank you,\nMatcHub Team";
    };

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
        email.setFrom(new InternetAddress(from, "MatcHub")); // Set the sender's email address.
        email.addRecipient(// Add recipient
                javax.mail.Message.RecipientType.TO, // Indicates that the recipient is the primary one
                new InternetAddress(to)); // Set the recipient's email address.
        email.setSubject(subject); // Set the email's subject.
        email.setText(bodyText); // Set the text of the email body.
        return email; // Return the created email
    }

}
