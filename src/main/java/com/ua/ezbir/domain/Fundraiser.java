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

    // Поле типу Set<Category> для зберігання множини категорій
    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "fundraiser_category", joinColumns = @JoinColumn(name = "fundraiser_id"))
    @Column(name = "category")
    @Builder.Default
    private Set<Category> categories = new HashSet<>();

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private List<Post> posts = new ArrayList<>();
}