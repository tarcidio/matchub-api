# MatcHub API

![IntroAPI](demo/Intro.jpeg)

## Overview

MatcHub is an interactive platform for League of Legends players, providing a space where users can explore and discuss the advantages and disadvantages of each game matchup. This project was developed during an internship and includes several complementary modules. Detailed information is available at the [main repository](https://github.com/tarcidio/matchub). This document aims to summarize some of the functionalities of the project's API, showcasing aspects of the database and the available routes.

## Table of Content

* [Database](#database)
   * [Entity Relationship Model](#entity-relationship-model)
   * [Relational Model](#relational-model)
* [About the Security](#about-security)
   * [JSON Web Token (JWT)](#json-web-token-jwt)
   * [OAuth2.0](#oauth20)
* [Routes](#routes)

## Database

The Entity-Relationship (ER) model and the Relational model serve different purposes in the database design and implementation process. The ER model is primarily used for conceptual design, providing a high-level graphical representation of entities, attributes, and relationships. It helps in understanding and planning the structure of the database. On the other hand, the Relational model is used for the actual implementation of the database, focusing on how data is stored, queried, and manipulated using tables, columns, and keys.

The entity-relationship model and the relational model were the initial steps taken in the project to provide a clear vision of the development process. Since the main objective of the internship project was the development of the API itself and the frontend, the SQL code was not created initially; instead, the modeling was implemented using Spring JPA, a framework for database generation. 

As a future task, not only the tables will be implemented, but also queries, indexes, procedures, triggers, views, grants, and sessions to optimize the application's performance (see repository issues). The table system, based on the Relational Model, is generated almost immediately, reducing the need to focus on SQL development at the initial stage. 

Guidelines on some procedures and triggers, among other implementation details in Postgres, were attached in the entity-relationship model notes. Below, in addition to the images, there is also a brief textual explanation of the modeling.

### Entity Relationship Model

The database schema includes several main entities, each playing a crucial role in the application's functionality:

#### Main Entities:

- **HubUser**: Stores user information, affectionately called HubUser (**MatcHub** + `Users`) in this project, including username, email, and status (`Admin`, `Moderator`, `Guest`). Administrators have permissions to change user roles and delete accounts, while moderators can delete comments and block users.
- **Token**: [Manages authentication tokens (JWT)](#about-security), marking them as revoked or expired when necessary.
- **Champion**: Details the champions imported from the Riot platform, each with a unique ID and name.
- **Screen**: Represents the different _matchups_ that exist. It is noted as a weak entity, meaning its existence depends on the `Champion` entity to represent the main champion, or `spotlight`, and the opponent `champion`.
- **Comment**: Contains comments made by users, associated with a `HubUser` and a `Screen`. It includes attributes such as comment text, date, and count of likes and dislikes. The `Comment` entity is actually an aggregation generated from the relationship between `HubUser` and `Screen`, as a HubUser can comment on a `Screen`, and for the same `HubUser` and `Screen`, there can be multiple comments.
- **Evaluate**: Contains information about a `HubUser` evaluation of a comment. It will be mapped in its own table due to the N:N nature of the relationship.

#### Specific Features:

- **Comment and Evaluation Management**: Users can comment on different `Screen` and evaluate other comments. Comments can accumulate likes and dislikes. When a comment reaches a specific number of likes, the author receives a congratulatory notification via email.
- **Champion Selection**: Users with expertise in certain `Champion` have greater influence in the selection and evaluation of them. This is managed through the relationship between `HubUser` and `Champion`. A connection with the Riot API is planned to bring information about users' performance with specific `Champion`.

#### Model Image:

![MatcHub-MER](demo/MatcHub-MER-DarkMode.png)

### Relational Model

The relational model is a direct consequence of the entity-relationship model. Given the detailed description provided above, a separate textual explanation of the relational model is deemed unnecessary.

![MatcHub-Relational](demo/MatcHub-Relational-DarkMode.png)

To see Relational Model, acess [dbdiagram.io](https://dbdiagram.io/d/MatcHub-66796eb69939893dae1ac29b)

## About Security

OAuth2.0 is an authorization protocol that allows third-party applications to obtain limited access to a user's resources without exposing their credentials. In this application, it is used in conjunction with JWT Token.

### JSON Web Token (JWT)

JSON Web Token (JWT) is a token format, a data unit represented by a string containing specific information. The JWT consists of three parts concatenated and separated by dots: `header.payload.signature`. Let's detail each of these parts:

#### Header

The Header is a JSON that, after being converted to bytes and encoded in Base64Url, results in a string. It contains information about the type of token and the signing algorithm used (e.g., HMAC SHA256 or RSA).

Example:

1. Original JSON:

```json
{
   "alg": "HS256",
   "typ": "JWT"
}
```

2. Conversion to Bytes:

```
7b2022616c67223a20224853323536222c2022747970223a20224a575422207d
```

3. Encoding to Base64Url:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
```

#### Payload

The Payload is a JSON that, after being converted to bytes and encoded in Base64Url, results in a string. It contains the claims, which are information about an entity (usually the user) and additional data. Claims can include user authority, token expiration time, among other essential information, which can be standard or customized by the developer.

Example:

1. Original JSON:
   
```json
{
   "sub": "1234567890",
   "name": "John Doe",
   "admin": true,
   "iat": 1516239022
}
```

2. Conversion to Bytes:

```
7b0a202022737562223a202231323334353637383930222c0a2020226e616d65223a20224a6f686e20446f65222c0a20202261646d696e223a20747275652c0a202022696174223a20313531363233393032320a7d
```

3. Encoding to Base64Url:

```
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0
```

#### Signature

The Signature is a string created from the concatenation of the Header and Payload, separated by a dot, and then encoded using the signing algorithm defined in the Header.

Example:

1. Concatenation of Header and Payload:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0
```

2. Application of the HMAC SHA256 algorithm using `6596723733C6C56FE8BE222BD5B3A85AF717B6CBF5CE78DDDB57FEDE97` as the private key:

```
7d2c6d1f4e5b6f7a8c9d0e1f2a3b4c5d6e7f8g9h0i1j2k3l4m5n6o7p8q9r0s1t
```

By generating these three strings and concatenating them using dots, we obtain the complete JWT token:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.7d2c6d1f4e5b6f7a8c9d0e1f2a3b4c5d6e7f8g9h0i1j2k3l4m5n6o7p8q9r0s1t
```

This token is provided to the user by the API and must be sent in requests that require authentication. In the API, as will be explained later, this token is decoded and verified to ensure its validity. This includes checking if the token has not been altered, if it corresponds to a previously generated token, and if it is associated with a registered user.

### OAuth2.0

#### Login

After understanding the JWT architecture, it is possible to understand the implementation of the OAuth2.0 protocol in MatcHub with Spring. Imagine a user accessing the application without being logged in. By filling in the login screen with their username and password and clicking the login button ([see the application design](https://github.com/tarcidio/matchub-web)), the frontend sends a request to the application's API to obtain a JWT token. This token acts as a passport, allowing the user to make other requests to the API, such as accessing their information on the site, viewing comments on a matchup, among other actions.

Unlike the routes that provide access to information on the site, the API route for login is not protected, allowing any user to request the frontend to send a request with their credentials to obtain a JWT token from the API. The other routes generally require permission that can only be granted by sending a JWT token along with the request.

When the user requests the frontend to make the login request, the API triggers the AuthenticationManager, a Spring Security class, to access the HubUser table in the database. In Spring, the user class, such as HubUser, must implement the UserDetails interface. It is through this implementation that Spring Security knows which table to direct the AuthenticationManager to search. The AuthenticationManager securely verifies if the provided credentials match any record in the database table. In this application, the database management system used is Postgres.

If the credentials are correct, the API requests the JwtService class, implemented by the programmer, to create a JWT token with the appropriate specifications (username, role, expiration time, etc.) and returns it to the user's frontend. The frontend then stores the token in LocalStorage, the browser's local storage.

#### Application

Now, the user can perform various actions on the page, such as making comments, among others. Suppose they open the home page, where two champion selection boxes appear to choose a matchup. For this page to load, the user's frontend must make requests to the API, sending the token stored in the browser.

When making a request that requires permission, it is immediately forwarded to the JwtAuthFilter class, implemented by the programmer in the SecurityFilterChain class to be called in the Spring Security system, where the presence of the JWT token is verified. If the token is present, the JwtAuthFilter requests the JwtService class to validate if the token is indeed a JWT, if it is not expired, and to which user it belongs. If there is any problem at this stage, a 401 error is returned. Otherwise, the JwtAuthFilter requests the UserDetailService class to access the UserDetails table in the database to obtain the user's data (identified by the information obtained from the JwtService).

With the returned user data, the JwtAuthFilter confirms that everything is correct and updates the security context with the authenticated user's information, allowing the API to proceed with its part. The API then verifies if the authenticated user has permission to make the request in question by consulting the user's role in Spring Security. If the user does not have permission, such as a normal authenticated user trying to perform actions of an administrator user, the API returns a 403 error. Otherwise, the request is forwarded to the class corresponding to that route.

These classes that process requests after the authentication system are called controllers. The appropriate controller processes the request and provides the corresponding response with a 200 (OK) status.

#### AccessToken and RefreshToken

The token used to access protected routes is called an AccessToken. This token has a short lifespan, usually around 15 minutes, for security reasons. After this period, when making requests that require permission, the user will receive a 401 error. This is because if a malicious agent steals this token, they will have full access to the user's permissions, being able to perform actions on their behalf without consent. With a short lifespan, the impact of a possible theft is minimized.

But how does the user stay logged in for long periods? The RefreshToken is used, which is also a JWT. The RefreshToken is received along with the AccessToken, also created by the JwtService class, during login and is stored in the browser's cookies, not in LocalStorage. Cookies are small text files stored on the user's device (such as a computer, smartphone, or tablet) by the web browser. They are more secure and cannot be directly accessed by browser code.

Whenever an AccessToken expires, the user can make a request to refresh the AccessToken through a specific route that does not require permission. In this request, they send both the expired AccessToken and the RefreshToken. The API then requests the JwtService class to validate these tokens, and if everything is correct, issues a new AccessToken with the expiration time reset, allowing the user to continue using the application without needing to log in again.

#### Diagrama

Segue um resumo dos componentes principais do OAuth2.0 implementado e uma imagem com o fluxo de dados:

**Componentes Principais:**

1. **HTTP Requests**: Representa as requisições HTTP que chegam ao sistema.
2. **JwtAuthFilter**: Um filtro que intercepta as requisições HTTP para verificar a presença e a validade do token JWT.
3. **JwtService**: Serviço responsável por validar o token JWT.
4. **UserDetailsService**: Serviço que carrega os detalhes do usuário a partir do banco de dados.
5. **SecurityContextHolder**: Atualiza o contexto de segurança com as informações do usuário autenticado.
6. **DispatcherServlet**: Encaminha as requisições para os controladores apropriados.
7. **Controller**: Controladores que processam as requisições e retornam as respostas.
8. **PostgreSQL (Docker)**: Banco de dados PostgreSQL rodando em um contêiner Docker.
9. **Apache Tomcat**: Servidor de aplicação que hospeda o Spring Boot.
10. **Spring Boot**: Framework que facilita a criação de aplicações Java.

**Fluxo de Dados:**

![OAuth2.0](demo/JwtSystem.png)

## Routes

Below is a detailed list of the routes available in the application, categorized by their respective controllers. Each route includes the HTTP method, endpoint, and a brief description of its functionality.

### ScreenController
1. **GET /screens/{id}**
   - Returns the details of a specific screen by its ID.

2. **GET /screens/{spotlightId}/{opponentId}**
   - Returns the details of a specific screen based on the IDs of the spotlight and opponent champions.

### ModeratorController
1. **PUT /moderators/{hubUserid}**
   - Blocks a specific user by their ID.

2. **DELETE /moderators/{commentId}**
   - Deletes a specific comment by its ID.

### HubUserController
1. **GET /hubusers/{id}**
   - Returns the details of a specific user by their ID.

2. **GET /hubusers**
   - Returns the details of the currently logged-in user.

3. **PUT /hubusers**
   - Updates the details of the currently logged-in user.

4. **PATCH /hubusers/change-password**
   - Changes the password of the currently logged-in user.

5. **POST /hubusers**
   - Uploads an image for the profile of the currently logged-in user.

6. **PATCH /hubusers/reset-password**
   - Resets the password of the currently logged-in user.

7. **PATCH /hubusers/confirm**
   - Confirms the email of the currently logged-in user.

### EvaluationController
1. **POST /comments/{commentId}/evaluations**
   - Creates a new evaluation for a specific comment.

2. **PUT /comments/{commentId}/evaluations/{evaluationId}**
   - Updates a specific evaluation of a comment.

3. **DELETE /comments/{commentId}/evaluations/{evaluationId}**
   - Deletes a specific evaluation of a comment.

### CommentController
1. **GET /screens/{screenId}/comments/{commentId}**
   - Returns the details of a specific comment by its ID.

2. **POST /screens/{screenId}/comments**
   - Creates a new comment for a specific screen.

3. **PUT /screens/{screenId}/comments/{commentId}**
   - Updates a specific comment of a screen.

4. **DELETE /screens/{screenId}/comments/{commentId}**
   - Deletes a specific comment of a screen.

### ChampionController
1. **GET /champions/{id}**
   - Returns the details of a specific champion by its ID.

2. **GET /champions**
   - Returns a list of all champions.

### AdminController
1. **PUT /admins/{hubUserid}**
   - Changes the position of a specific user by their ID.

2. **DELETE /admins/{hubUserId}**
   - Deletes a specific user by their ID.

### AuthController
1. **POST /auth/register**
   - Registers a new user and sends a confirmation email.

2. **POST /auth/authenticate**
   - Authenticates a user and returns a JWT token.

3. **POST /auth/refresh-token**
   - Refreshes the JWT token.

4. **POST /auth/forgot-password**
   - Sends an email for password reset.

### RiotAPIController (under construction 🚧🚧🚧🔨🔨🔨)
1. **GET /rso/link**
   - Returns a link for authentication with the Riot API.

2. **GET /rso/integrate**
   - Integrates user data with the Riot API using an authorization code.