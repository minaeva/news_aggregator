package com.nfa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_entity")
@Data
@NoArgsConstructor
public class NewsUserEntity {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private NewsUserRole role;

    @Column(name = "registration_source")
    @Enumerated(EnumType.STRING)
    private RegistrationSource registrationSource;

    public NewsUserEntity(String name, String email, String password, RegistrationSource registrationSource) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.registrationSource = registrationSource;
    }
}
