package com.example.urlshorteningservice.service;

import com.example.urlshorteningservice.model.Url;
import com.example.urlshorteningservice.repository.UrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Service
public class UrlService {
    private UrlRepository repo;

    public Url createUrl (String url) {
        String shortCode = generateUniqueShortCode ();
        Url urlObject = new Url (url, shortCode);
        return this.repo.save (urlObject);
    }

    public Optional <Url> getUrlByShortCode (String shortCode) {
        return this.repo.findByShortCode (shortCode);
    }

    public Url updateUrl (String shortCode, String url) {
        Url urlObject = getUrlByShortCode (shortCode)
                .orElseThrow (() -> new RuntimeException ("Invalid short code"));
        urlObject.setUrl (url);
        return this.repo.save (urlObject);
    }

    public void deleteUrl (String shortCode) {
        Url urlObject = getUrlByShortCode (shortCode)
                .orElseThrow (() -> new RuntimeException ("Invalid short code"));
        this.repo.delete (urlObject);
    }

    public int getAccessCount (String shortCode) {
        Url urlObject = getUrlByShortCode (shortCode)
                .orElseThrow (() -> new RuntimeException ("Invalid short code"));
        return urlObject.getAccessCount ();
    }

    public void incrementAccessCount (Url url) {
        url.setAccessCount (url.getAccessCount () + 1);
        this.repo.save (url);
    }

    private String generateUniqueShortCode () {
        String shortCode = generateShortCode ();

        while (getUrlByShortCode (shortCode).isPresent ()) {
            shortCode = generateShortCode ();
        }

        return shortCode;
    }

    private String generateShortCode () {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder ();
        Random random = new Random ();

        for (int i = 0; i < 6; i++) {
            char chosenCharacter = characters.charAt (random.nextInt (characters.length ()));
            stringBuilder.append (chosenCharacter);
        }

        return stringBuilder.toString ();
    }
}
