package com.nfa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription")
@Data
@NoArgsConstructor
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "source")
    private String source;

    @Column(name = "category")
    private String category;

    @Column(name = "keywords")
    private String keywords;

    @OneToOne(mappedBy = "subscription", cascade =
            {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private NewsUserEntity user;

    public SubscriptionEntity(String source, String category, String keywords) {
        this.source = source;
        this.category = category;
        this.keywords = keywords;
    }
}
