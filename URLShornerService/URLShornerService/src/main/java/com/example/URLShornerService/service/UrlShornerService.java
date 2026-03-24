package com.example.URLShornerService.service;

import com.example.URLShornerService.entity.ShortUrl;
import com.example.URLShornerService.repository.ShortUrlRepository;
import com.example.URLShornerService.util.ShortCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UrlShornerService {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private ShortCodeGenerator shortCodeGenerator;

    private static final int MAX_RETRIES = 10;

    /**
     * Creates a shortened URL. If the original URL already exists, returns the existing short code.
     * Generates a unique short code and handles collisions by retrying.
     *
     * @param originalUrl The original URL to shorten
     * @return The ShortUrl entity with the generated short code
     */
    @Transactional
    public ShortUrl createShortUrl(String originalUrl) {
        // Check if URL already exists
        Optional<ShortUrl> existingUrl = shortUrlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        // Generate unique short code (handle collisions)
        String shortCode = generateUniqueShortCode();

        // Create and save new ShortUrl entity
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setShortcode(shortCode);

        return shortUrlRepository.save(shortUrl);
    }

    /**
     * Retrieves the original URL by looking up the short code.
     *
     * @param shortcode The short code to lookup
     * @return An Optional containing the ShortUrl if found
     */
    @Transactional(readOnly = true)
    public Optional<ShortUrl> getOriginalUrl(String shortcode) {
        return shortUrlRepository.findByShortcode(shortcode);
    }

    /**
     * Generates a unique short code by retrying if collision occurs.
     *
     * @return A unique short code
     * @throws RuntimeException if unable to generate unique code after max retries
     */
    private String generateUniqueShortCode() {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            String shortCode = shortCodeGenerator.generateShortCode();

            // Check if this short code already exists
            Optional<ShortUrl> existing = shortUrlRepository.findByShortcode(shortCode);
            if (existing.isEmpty()) {
                return shortCode;
            }
        }

        // If we've exhausted retries, throw an exception
        throw new RuntimeException("Failed to generate unique short code after " + MAX_RETRIES + " attempts");
    }
}

