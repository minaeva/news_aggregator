package com.nfa.controller;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class AuthRequest {

    private String email;
    private String password;
}
