package com.nfa.controller.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class AuthRequest {

    private String email;
    private String password;
}
