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
    // Instância de `JsonFactory` que é usada para processar (serializar e desserializar) dados JSON
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // Escopos de permissão que sua aplicação requer para acessar a API do Gmail.
    // Neste caso, `GmailScopes.GMAIL_SEND` permite que a aplicação envie e-mails em nome do usuário autenticado
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);

    // Caminho do diretório onde os tokens de autenticação OAuth2 são armazenados localmente
    @Value("${gmail.api.tokens}")
    private String TOKENS_DIRECTORY_PATH;

    // Caminho para o arquivo JSON que contém as credenciais da API do Google
    @Value("${gmail.api.credentials}")
    private String CREDENTIALS_FILE_PATH;

    // Constante armazena o nome da aplicação que será usado ao configurar o cliente da API do Gmail.
    // Este nome é geralmente utilizado para fins de logging e monitoramento no Google Cloud Platform.
    @Value("${gmail.api.app.name}")
    private String APPLICATION_NAME;

    public Gmail gmailServiceFactory() throws IOException, GeneralSecurityException {
        /*
        NetHttpTransport: classe usada para configurar e executar solicitações HTTP de baixo nível
            Origem: biblioteca Google API Client
            Detalhes: fornece uma implementação de transporte HTTP que utiliza a biblioteca `java.net` do
            Java para realizar as operações de rede
        */
        // final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        /*
        Gmail: classe usado para realizar operações suportadas pela API do Gmail
            Origem: API do Gmail que segue o padrão de design Builder (construir objetos complexos passo a passo)
            Utilidade: enviar e-mails, gerenciar mensagens, etc.
        */
        return new Gmail.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                getCredentials(HTTP_TRANSPORT)
        ).setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Abre o arquivo credencials
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);
        FileInputStream credentialsStream = new FileInputStream(credentialsFile);

        // GoogleClientSecrets: encapsula as credenciais do cliente
        // Origem: classe da biblioteca Google API Client
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(credentialsStream));

        // GoogleAuthorizationCodeFlow: gerencia fluxo de autorização OAuth 2.0 para
            // Utilidade: usado para iniciar o processo de autorização, onde o usuário é redirecionado
            // para uma página de login do Google para conceder permissões à aplicação
        // aplicações que acessam APIs protegidas do Google
        // setDataStoreFactory: configura onde os tokens de autorização serão armazenados localmente
        // FileDataStoreFactory: armazena os tokens em um sistema de arquivos local
        // setAccessType("offline"): acessar os recursos do usuário quando o usuário não está presente na sessão
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        // AuthorizationCodeInstalledApp: gerencia o fluxo de autorização onde o usuário precisa
        // conceder permissões à aplicação (classe da biblioteca Google API Client)
        // LocalServerReceiver: cria um servidor HTTP local temporário usado para
        // escutar a resposta do Google após o usuário autorizar a aplicação
        // .authorize("user"): inicia o processo de autorização
        // "user": identificador para diferenciar usuários no armazenamento de tokens,
        // mas como há apenas um usuário, ele não é essencialmente útil neste contexto

        /* Funcionamento
        - Quando chamado, `authorize` abre o navegador padrão do usuário para a URL de autorização do Google, onde o usuário pode logar e conceder as permissões solicitadas pela aplicação.
      - Após o usuário conceder as permissões, o Google redireciona o navegador para o `LocalServerReceiver`. O servidor local captura o código de autorização do redirecionamento e o `AuthorizationCodeInstalledApp` usa esse código para solicitar tokens de acesso e refresh tokens do Google.
      - Os tokens obtidos são então armazenados no local especificado pelo `GooglesAuthorizationCodeFlow` e podem ser usados para autenticar chamadas à API do Google.
        */
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        // Configura o receptor para usar a porta 8080 e o caminho /oauth2callback
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
//                .setPort(8080)
//                .setHost("localhost")
//                .setCallbackPath("/oauth2callback")
//                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    // Message: representa uma mensagem de e-mail (classe da API do Gmail)
    /* Diferença de MimeMessage e Message
    MimeMessage: cria e manipular mensagens de e-mail no padrão de email da internet
    Message: representa uma mensagem no Gmail e interage com o Gmail API
    */
    public Message sendMessage(Gmail service, String userId, MimeMessage emailContent) throws MessagingException, IOException {
        // ByteArrayOutputStream: armazena dados em memória (classe do pacote `java.io`)
        // buffer se expande automaticamente para acomodar os dados
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        // writeTo: escrever conteúdo do MimeMessage para o OutputStream
        emailContent.writeTo(buffer);

        // Converte dados acumulados em um array de bytes
        byte[] bytes = buffer.toByteArray();
        // Base64:  fornece métodos para codificar e decodificar dados usando codificação Base64
        // Codifica o array de bytes fornecido em uma string Base64
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);

        Message message = new Message(); // Cria Message que será usado para enviar o e-mail
        message.setRaw(encodedEmail); // Define o conteúdo codificado do e-mail
        /*
        users: recurso que representa os usuários na API do Gmail
        messages: retorna um recurso que representa as mensagens de e-mail dentro da conta do usuário
        */
        message = service.users().messages().send(userId, message).execute(); // Envia o e-mail
        return message;
    }

}