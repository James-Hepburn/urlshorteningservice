package com.example.urlshorteningservice.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String shortCode;

    private String url;
    private int accessCount;

    public Url (String url, String shortCode) {
        this.url = url;
        this.shortCode = shortCode;
        this.accessCount = 0;
    }
}
