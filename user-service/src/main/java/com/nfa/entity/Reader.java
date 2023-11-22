package com.nfa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "reader")
@Data
@NoArgsConstructor
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ReaderRole role;

    @Column(name = "registration_source")
    @Enumerated(EnumType.STRING)
    private RegistrationSource registrationSource;

    @Column(name = "times_per_day")
    private int timesPerDay;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "reader_keyword",
            joinColumns = @JoinColumn(name = "reader_email"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )
    private Set<Keyword> keywords;

    public Reader(String name, String email, String password, RegistrationSource registrationSource) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.registrationSource = registrationSource;
    }
}
