package com.nfa.config;

import com.nfa.dto.ReaderDto;
import com.nfa.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import static com.nfa.config.CustomNewsUserDetails.fromNewsUserDtoToCustomUserDetails;

@Component
public class CustomNewsUserDetailsService implements UserDetailsService {

    private final ReaderService readerService;

    @Autowired
    CustomNewsUserDetailsService(@Lazy ReaderService readerService) {
        this.readerService = readerService;
    }

    @Override
    public CustomNewsUserDetails loadUserByUsername(String email) {
        ReaderDto readerDto = readerService.findByEmail(email);
        return fromNewsUserDtoToCustomUserDetails(readerDto);
    }

}
