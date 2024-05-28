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
import java.security.GeneralSecurityException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final GmailService gmailService;

    @Value("${gmail.api.from}")
    private String FROM_GMAIL_API;

    @Value("${gmail.api.from}")
    private String LINK_APP_RESET_PASSWORD;

    public void sendRecoveryEmail(String emailHubUser, String token) throws IOException, MessagingException, GeneralSecurityException {
        // Cria service que gerenciará envio de email
        Gmail service = gmailService.gmailServiceFactory();

        // Descreve informações do e-mail
        String user = "me";
        String to = emailHubUser;
        String from = FROM_GMAIL_API;
        String subject = "Password Reset Request";
        String resetLink = "https://" + LINK_APP_RESET_PASSWORD + "?token=" + token;
        String bodyText = "Please click on the following link to reset your password: " + resetLink;

        // Cria objeto e-mail
        MimeMessage emailContent = createEmail(to, from, subject, bodyText);
        // Enviar o e-mail
        gmailService.sendMessage(service, user, emailContent);
    }

    // MimeMessage: constrói e configura detalhes de um e-mail (classe da Java Mail API)
    public static MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        // Properties: usado para configurar parâmetros para a sessão de e-mail
        Properties props = new Properties();
        // Session: representa uma sessão de correio, agrupando todas as configurações e
        // propriedades necessárias para o envio de e-mails, como
        // informações de servidor SMTP, autenticação, etc. (classe da Java Mail API)
        // getDefaultInstance: obtem instância da Session com essas propriedades ou cria caso não exista
        // null: fornece credenciais quando o servidor de e-mail requer autenticação
        // Passar `null` significa que não há autenticação personalizada sendo fornecida
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session); // Cria MimeMessag` usando a sessão criada
        email.setFrom(new InternetAddress(from)); // Define o endereço do remetente do e-mail.
        email.addRecipient(// Adiciona destinatário
                javax.mail.Message.RecipientType.TO, // Indica que o destinatário é o principal
                new InternetAddress(to)); // Define o endereço do destinatário do e-mail.
        email.setSubject(subject); // Define o assunto do e-mail.
        email.setText(bodyText); // Define o texto do corpo do e-mail.
        return email; // Retorna o email criado
    }

}
