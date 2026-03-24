package com.example.URLShornerService.controller;

import com.example.URLShornerService.dto.ShortenUrlRequest;
import com.example.URLShornerService.dto.ShortenUrlResponse;
import com.example.URLShornerService.entity.ShortUrl;
import com.example.URLShornerService.service.UrlShornerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UrlShornerService urlShornerService;

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("shortenUrlRequest", new ShortenUrlRequest());
        return "index";
    }

    @PostMapping("/shorten")
    public String shortenUrl(@ModelAttribute ShortenUrlRequest request, Model model) {
        ShortUrl shortUrl = urlShornerService.createShortUrl(request.getUrl());
        ShortenUrlResponse response = new ShortenUrlResponse();
        response.setShortcode(shortUrl.getShortcode());
        response.setOriginalUrl(shortUrl.getOriginalUrl());
        response.setCreatedAt(shortUrl.getCreatedAt());
        response.setExpiresAt(shortUrl.getExpiresAt());
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        response.setShortUrl(baseUrl + "/" + shortUrl.getShortcode());
        model.addAttribute("response", response);
        model.addAttribute("shortenUrlRequest", request); // to keep the form filled
        return "index";
    }

    @GetMapping("/{shortCode}")
    public String redirectToUrl(@PathVariable String shortCode) {
        Optional<ShortUrl> shortUrlOpt = urlShornerService.getOriginalUrl(shortCode);
        if (shortUrlOpt.isPresent()) {
            return "redirect:" + shortUrlOpt.get().getOriginalUrl();
        } else {
            return "redirect:/"; // or 404, but for simplicity
        }
    }
}
