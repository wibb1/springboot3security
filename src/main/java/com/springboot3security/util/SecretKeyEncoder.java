package com.springboot3security.util;

import java.util.Base64;

public class SecretKeyEncoder {

    private static final String secret = "gdkj3lv9mz7pn4xqsu5waf82hrtvy0cj6bkoqx1niead3mpzvluw9ytfcork8xhs";

    public static void main(String[] args) {
        if (secret == null) {
            throw new IllegalArgumentException("secret cannot be null");
        }
        // Encode the secret key in Base64
        String base64EncodedKey = Base64.getEncoder().encodeToString(secret.getBytes());
        System.out.println("Base64 Encoded Key:");
        System.out.println(base64EncodedKey);
    }
}
