package com.ua.ezbir.domain;

import com.ua.ezbir.web.fundraiser.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fundraisers")
public class Fundraiser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    private String name;

    @Column(name = "jar_link")
    private String jarLink;

    private String description;

    @Column(name = "is_closed")
    @Builder.Default
    private boolean isClosed = false;

    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @ManyToOne
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Set<Category> categories = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "fundraiser")
    private List<Post> posts = new ArrayList<>();
}