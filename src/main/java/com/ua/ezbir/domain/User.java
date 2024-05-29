package com.ua.ezbir.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;

    private String password;

    private String description;

    private long number;

    @Column(name = "photo_url")
    private String photoUrl;

    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Builder.Default
    private boolean enabled = false;

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "fundraiser_id", referencedColumnName = "id")
    private List<Fundraiser> fundraisers = new ArrayList<>();
}