package com.springboot3security.util;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;

public class SecretKeyEncoder {
    public static void main(String[] args) {
        try {
            // Load the application.properties file
            Properties properties = new Properties();
            String propertiesPath = Paths.get("src/main/resources/application.properties").toAbsolutePath().toString();
            properties.load(new FileInputStream(propertiesPath));

            // Get the jwt.secret value
            String secret = properties.getProperty("jwt.secret");
            if (secret == null) {
                throw new IllegalArgumentException("jwt.secret not found in application.properties");
            }
            // Encode the secret key in Base64
            String base64EncodedKey = Base64.getEncoder().encodeToString(secret.getBytes());
            System.out.println("Base64 Encoded Key:");
            System.out.println(base64EncodedKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
