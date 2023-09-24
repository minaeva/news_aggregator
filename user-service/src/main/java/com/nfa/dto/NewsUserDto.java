package com.nfa.dto;

import com.nfa.entity.NewsUserRole;
import com.nfa.entity.RegistrationSource;
import lombok.Data;

@Data
public class NewsUserDto {

    private Long id;

    private String name;

    private String email;

    private String password;

    private NewsUserRole role;

    private RegistrationSource registrationSource;

}
