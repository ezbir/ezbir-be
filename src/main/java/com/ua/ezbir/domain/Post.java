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
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @ManyToOne
    private Fundraiser fundraiser;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "post_content_urls", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "content_url")
    private List<String> contentUrls = new ArrayList<>();
}
