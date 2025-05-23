# Table of Contents
1. [Spring Boot 3 Security Application](#spring-boot-3-security-application)
2. [Features](#features)
3. [Technologies Used](#technologies-used)
4. [Quick Start](#quick-start)
5. [Project Structure](#project-structure)
6. [Security](#security)
    - [Authentication](#authentication)
    - [Authorization](#authorization)
    - [Security Configuration](#security-configuration)
    - [Summary of Security Flow](#summary-of-security-flow)
    - [Testing Security](#testing-security)
7. [Configuration](#configuration)
8. [How to Run](#how-to-run)
9. [Testing](#testing)
10. [Endpoints](#endpoints)
11. [Logging](#logging)

# Spring Boot 3 Security Application

This project is a Spring Boot 3 application that demonstrates the implementation of security features, including user
authentication and authorization using JWT (JSON Web Tokens). It includes a custom user management system and integrates
with Spring Security.  I completed this as a prototype for the fishing-log3 application, which is a web application for
logging fishing trips. The fishing-log3 application is a work in progress, and this project serves as a proof of concept
for the security features that will be implemented in the fishing-log3 application.

## Features

- **Authentication & Authorization**:
    - Secure login using JWT.
    - Role-based access control (USER and ADMIN roles).
    - Custom `UserDetails` implementation for user data mapping.

- **Password Security**:
    - Password hashing with `PasswordEncoder`.
    - Validation for strong password requirements.

- **Testing**:
    - Unit tests for services and components.
    - Security tests for JWT validation and role-based access.
    - Role-based endpoint access tests.
    - Comprehensive test coverage for security features.

## Technologies Used

- **Java 17**: Core programming language.
- **Spring Boot 3**: Framework for building the application.
- **Spring Security 6**: For authentication and authorization.
- **JWT**: Token-based authentication.
- **Maven**: Build and dependency management tool.
- **JUnit 5**: For unit testing.
- **PostgresSQL**: Database for storing user data.

## Quick Start

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/wibb1/springboot3security.git
   cd springboot3security
    ```
2. Set Up the Database:
   - Install PostgresSQL. You can also use a different database, but you will need to update all applicable database related properties in the application.properties file.  For more details see [Configuration](#configuration) **Section 1** below.
   - Create a database named springbootreactjwt.
     ```SQL
     CREATE DATABASE springbootreactjwt;
     ```
   - Create a user with the same name as the database and grant all privileges to that user.
     ```SQL
     CREATE USER springbootreactjwt WITH PASSWORD '<your_password>';
      
     GRANT ALL PRIVILEGES ON DATABASE springbootreactjwt TO springbootreactjwt;
     ``` 
   - Update the `application.properties` file with your database credentials.
   - Generate a base64 encoded key for the JWT secret. For more detail see [Configuration](#configuration) **Section 2 and 3** below.
3. Build the Project:
    ```bash
    mvn clean install
    ```
4. Run the Application:
    ```bash
    mvn spring-boot:run
    ```
5. Test the Endpoints:
    - Use Postman or curl to test the endpoints, for more details and images of the Postman setup see the [Endpoints](#endpoints) section below.

## Project Structure

- `src/main/java`: Contains the main application code.
    - `com.springboot3security.entity`: Entity classes for user data.
    - `com.springboot3security.service`: Service classes for business logic.
    - `com.springboot3security.filter`: JWT authentication filter.
    - `com.springboot3security.util`: Utility classes for JWT handling.
- `src/test/java`: Contains unit tests for the application.
- `src/main/resources`: Configuration files (e.g., `application.properties`).

## Security

This project implements robust security features using **Spring Security 6** and **JWT (JSON Web Tokens)** for authentication and authorization.

### Authentication

- **JWT-Based Authentication**:
    - Users authenticate by sending their credentials to `/auth/generateToken`.
    - A JWT is generated and returned if the credentials are valid.
    - The JWT contains user details and roles, signed with a secret key.
    - Clients include the JWT in the `Authorization` header (prefixed with `Bearer `) for secure requests.

- **Password Security**:
    - Passwords are hashed using `PasswordEncoder` before being stored in the database.
    - Password validation ensures strong passwords (e.g., minimum length, special characters).

### Authorization

- **Role-Based Access Control**:
    - User roles (e.g., `USER`, `ADMIN`) are stored in the database and mapped to `GrantedAuthority` objects.
    - Access to endpoints is restricted based on roles using Spring Security.

- **Securing Endpoints**:
    - Public endpoints (e.g., `/auth/welcome`) are accessible without authentication.
    - Secure endpoints require a valid JWT and are restricted based on roles:
        - `/auth/user`: Accessible only to users with the `USER` role.
        - `/auth/admin`: Accessible only to users with the `ADMIN` role.

### Security Configuration

- **JWT Authentication Filter**:
    - A custom filter validates the JWT and sets the authentication context for secure requests.
    - Invalid or missing JWTs result in access denial.

- **Method-Level Security**:
    - Enabled using `@EnableGlobalMethodSecurity(prePostEnabled = true)`.
    - Allows fine-grained access control with `@PreAuthorize`.

### Summary of Security Flow

1. **User Authentication**:
    - The user sends a POST request to `/auth/generateToken` with their credentials.
    - If valid, a JWT is returned.

2. **Accessing Secure Endpoints**:
    - The client includes the JWT in the `Authorization` header for secure requests.
    - The server validates the JWT and checks the user's roles before granting access.

3. **Role-Based Access**:
    - The `@PreAuthorize` annotation ensures that only users with the appropriate roles can access specific endpoints.
   
### Testing Security

The application includes dedicated tests for security features:

- **Preauthorization Tests**:
    - Tests endpoint access with different user roles
    - Verifies proper authorization enforcement
    - Confirms role-based access restrictions

- **Authentication Tests**:
    - Tests JWT token generation and validation
    - Verifies user authentication flows
    - Tests invalid authentication scenarios

## Configuration

1. **application.properties**: Configuration file for database connection, JWT secret key, and other properties.
    - Example file available as `src/main/resources/application.properties.rename`:
    - Rename it to `application.properties` and update the values as needed.
    - Edit the database connection properties:
      ```properties
      spring.datasource.driver-class-name=org.postgresql.Driver
      spring.datasource.url=jdbc:postgresql://localhost:5432/springbootreactjwt
      spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
      spring.datasource.username=springbootreactjwt
      spring.datasource.password=<your_password>
      ```
    - Update log file location
      ```properties
      logging.file.path=<your_path_to_log_file>
      ```
2. Generate Base64 Key:
    - To generate a base64 encoded key, you can ask an AI to generate one (`generate a base64 encoded key value with 64 bytes`) for you or use the SecretKeyEncoder to generate one
    - To use `SecretKeyEncoder` (A utility java class in the utilities package), enter a starting key in the `secret` value 64 characters long, there is a sample value entered. 
      ```java
      private static final String secret = "gdkj3lv9mz7pn4xqsu5waf82hrtvy0cj6bkoqx1niead3mpzvluw9ytfcork8xhs";
      ```
    - Then compile SecretKeyEncoder.java
      ```bash 
      javac src/main/java/com/springboot3security/util/SecretKeyEncoder.java
      ```
    - Run the class with the key value you entered in `jwt.secret` as an argument
      ```bash
      java -cp src/main/java com.springboot3security.util.SecretKeyEncoder
      ```
3. ***IMPORTANT!*** --> Reformat the generated key:
    - Remove any `=` characters at the end of the key, these are padding characters, will cause errors, and are not needed. 
     The output line from the SecretKeyEncoder run or AI should be copied to the `jwt.secret` property, replacing the current value.  
     ```properties
     jwt.secret=<your_secret_key_64_characters_long>
     ```
  
## How to Run

### Clone the repository:
```bash
git clone https://github.com/wibb1/springboot3security.git
cd springboot3security
```
### Build the project using Maven:
```bash
mvn clean install
```
### Run the application:
```bash
mvn spring-boot:run
```
or run the JAR file:
```bash
java -jar target/spring-boot-3-security-0.0.1-SNAPSHOT.jar
```
Access the application at http://localhost:8080.

## Testing
The application includes comprehensive test coverage:

- **Controller Tests**:
    - `UserControllerTest`: Tests basic controller functionality
    - `UserControllerPreauthorizedTest`: Tests role-based access control

- **Security Tests**:
    - Tests JWT token generation and validation
    - Tests authentication and authorization flows
    - Tests role-based endpoint access

- **Running Tests**:
    ```bash
    mvn test
    ```
    This will execute all the tests in the `src/test/java` directory.

## Endpoints

Use Postman or any other API testing tool to test the endpoints. The application runs on port 8080 by default.
### Welcome Endpoint:
- GET /auth/welcome: Returns a welcome message.
- http://localhost:8080/auth/welcome
- Response: `Welcome this endpoint is not secure`
- Status Code: `200 OK`
![Welcome response in Postman](readme_images/welcome-endpoint.png)
### Add New User Endpoint:
- POST /user: Adds a new user.
- http://localhost:8080/auth/addNewUser
- Request Body: JSON object with username, password, and role.
- Example:
  ```json
  {
    "username": "testuser",
    "password": "Password123",
    "role": "USER"
  }
  ```
- Response: `User added successfully` OR `User already exists`
- Status Code: `200 OK`
![Example of New User endpoint in Postman](readme_images/user-already-exists.png)
![Example of New Admin endpoint in Postman](readme_images/admin-already-exists.png)
### Generate Authentication Token:
- POST /authenticate: Authenticates a user and generates a JWT.
- http://localhost:8080/auth/generateToken
- Request Body: JSON object with username and password.
- Example:
  ```json
  {
    "username": "testuser",
    "password": "Password123"
  }
  ```
- Response: JWT token.
- Status Code: `200 OK`
![Example of Generating an Authentication Token in Postman](readme_images/user-generate-token.png)
### Decode JWT Token:
- Use jwt.io to decode the token and view the payload
- Copy the token from the response and paste it into the "Encoded Value" field on jwt.io.
- Copy the Secret value and paste into the "JWT Signature Verification" field on jwt.io.
- The payload will show the username (sub) and issue (iat) and expiration (exp) dates.
![Example of inputs for decoding the JWT on jwt.io](readme_images/jwt-io.png)
### Secure Endpoint for User Role:
- GET /user: Access restricted to users with the USER role.
- http://localhost:8080/auth/user
- Keys: JWT token in the Authorization header. Must be prefixed with "Bearer ".
![Example Response from an Authenticated User Endpoint](readme_images/user-secured-page.png)
### Secure Endpoint for Admin Role:
1. Secure Endpoint for Admin Role Hit By Authenticated User
   - GET /admin: Access restricted to users with the ADMIN role.
   - http://localhost:8080/auth/admin
   - Keys: JWT token in the Authorization header. Must be prefixed with "Bearer ".
![Example Response from an Authenticated Admin User Endpoint](readme_images/admin-secured-page.png)
2. Secure Endpoint for Admin Role Hit By Authenticated User
   - GET /admin: Access restricted to users with the ADMIN role.
   - http://localhost:8080/auth/admin
   - Keys: JWT token in the Authorization header. Must be prefixed with "Bearer ".
   - Response: `403 Forbidden`
![Example Response when attempting Unauthorized Access](readme_images/unauthorized-user.png)
## Logging

The application uses Spring Boot's logging framework to generate logs for debugging and monitoring purposes. By default, the logs are stored in a directory named `logs` in the root of the project.

### Configuration
- The log file path is configured in the `application.properties` file:
  ```properties
  logging.file.path=logs
  ```
- This will create a logs directory in the root of the project when the application runs. The log files will be stored in this directory.
### Log Levels
- The default log level is INFO. You can adjust the log level by adding the following property to the application.properties file:
```properties
logging.level.root=DEBUG
```
- You can also configure log levels for specific packages:
```properties
logging.level.com.springboot3security=DEBUG
```
### Viewing Logs
- Logs are written to files in the logs directory. Open the log files to view application activity, errors, and debug information.
### Customizing Logs
- To customize the logging format or behavior, refer to the Spring Boot Logging Documentation.