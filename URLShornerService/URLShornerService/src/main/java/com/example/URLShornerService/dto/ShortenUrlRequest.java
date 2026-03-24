package com.example.URLShornerService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortenUrlRequest {

    @NotBlank(message = "URL cannot be blank")
    @Pattern(
        regexp = "^(https?|ftp)://[^\\s]+$",
        message = "Please provide a valid URL (must start with http://, https://, or ftp://)"
    )
    private String url;
}

