package com.example.URLShornerService.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component
public class ShortCodeGenerator {

    private static final String BASE62_CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SHORT_CODE_LENGTH = 6;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generates a random 6-character short code using Base62 characters.
     *
     * @return A random 6-character string using Base62 charset (0-9, a-z, A-Z)
     */
    public String generateShortCode() {
        StringBuilder shortCode = new StringBuilder(SHORT_CODE_LENGTH);

        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            int randomIndex = SECURE_RANDOM.nextInt(BASE62_CHARSET.length());
            shortCode.append(BASE62_CHARSET.charAt(randomIndex));
        }

        return shortCode.toString();
    }
}

