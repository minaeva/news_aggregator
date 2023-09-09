package com.nfa.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "article")
@Data
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "title")
    String title;
    @Column(name = "description")
    String description;
    @Column(name = "url")
    String url;
    @Column(name = "date_added")
    @CreationTimestamp
    Date dateAdded;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "source_id")
    Source source;
    @ManyToMany(
            cascade = {CascadeType.ALL})
    @JoinTable(
            name = "article_category",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    List<Category> categories;

    public Article(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
