package com.example.URLShornerService.repository;

import com.example.URLShornerService.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortcode(String shortcode);

    Optional<ShortUrl> findByOriginalUrl(String originalUrl);
}

