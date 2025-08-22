package com.example.urlshorteningservice.controller;

import com.example.urlshorteningservice.model.Url;
import com.example.urlshorteningservice.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/shorten")
@AllArgsConstructor
public class UrlController {
    private UrlService service;

    @PostMapping
    public ResponseEntity <?> createShortUrl (@RequestBody Map <String, String> request) {
        String url = request.get ("url");

        if (url == null || url.isBlank ()) {
            return ResponseEntity.badRequest ().body (Map.of ("error", "URL must not be empty"));
        }

        Url urlObject = this.service.createUrl (url);
        return ResponseEntity.status (201).body (urlObject);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity <?> getOriginalUrl (@PathVariable String shortCode) {
        Optional <Url> urlOptioonal = this.service.getUrlByShortCode (shortCode);

        if (urlOptioonal.isPresent ()) {
            Url url = urlOptioonal.get();
            this.service.incrementAccessCount (url);
            Url urlObject = this.service.getUrlByShortCode (shortCode).get ();
            return ResponseEntity.ok (urlObject);
        } else {
            return ResponseEntity.status (404).body (Map.of ("error", "Invalid short code"));
        }
    }

    @PutMapping("/{shortCode}")
    public ResponseEntity <?> updateUrl (@PathVariable String shortCode, @RequestBody Map <String, String> request) {
        String url = request.get ("url");

        if (url == null || url.isBlank ()) {
            return ResponseEntity.badRequest ().body (Map.of ("error", "Invalid URL"));
        }

        try {
            Url urlObject = this.service.updateUrl (shortCode, url);
            return ResponseEntity.ok (urlObject);
        } catch (RuntimeException e) {
            return ResponseEntity.status( 404).body (Map.of ("error", e.getMessage ()));
        }
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity <?> deleteUrl (@PathVariable String shortCode) {
        try {
            this.service.deleteUrl (shortCode);
            return ResponseEntity.noContent ().build ();
        } catch (RuntimeException e) {
            return ResponseEntity.status (404).body (Map.of ("error", e.getMessage ()));
        }
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity <?> getUrlStats (@PathVariable String shortCode) {
        return this.service.getUrlByShortCode (shortCode)
                .map (url -> Map.of (
                        "id", url.getId (),
                        "url", url.getUrl (),
                        "shortCode", url.getShortCode (),
                        "accessCount", url.getAccessCount ()
                ))
                .map (ResponseEntity::ok)
                .orElseGet (() -> ResponseEntity.status (404).body (Map.of ("error", "Invalid short code")));
    }
}
