package com.nfa.controller;

import com.nfa.entity.RegistrationSource;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class RegistrationRequest {

    private String name;
    private String email;
    private String password;
    private RegistrationSource registrationSource;
}
