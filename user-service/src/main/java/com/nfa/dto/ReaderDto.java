package com.nfa.dto;

import com.nfa.entity.ReaderRole;
import com.nfa.entity.RegistrationSource;
import lombok.Data;

@Data
public class ReaderDto {

    private Long id;

    private String name;

    private String email;

    private String password;

    private ReaderRole role;

    private RegistrationSource registrationSource;

}
