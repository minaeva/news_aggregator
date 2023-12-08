package com.nfa.dto;

import com.nfa.entity.ReaderRole;
import com.nfa.entity.RegistrationSource;

public record ReaderDto(Long id,
                        String name, String password,
                        String email,
                        ReaderRole role,
                        RegistrationSource registrationSource) {
}
