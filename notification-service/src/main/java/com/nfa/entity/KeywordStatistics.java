package com.nfa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "keyword_statistics")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class KeywordStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_created")
    @CreatedDate
    private LocalDateTime dateCreated;

    public KeywordStatistics(String name) {
        this.name = name;
    }

    @PrePersist
    public void prePersist() {
        if (dateCreated == null) {
            dateCreated = LocalDateTime.now();
        }
    }
}

