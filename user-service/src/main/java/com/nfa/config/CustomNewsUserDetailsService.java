package com.nfa.config;

import com.nfa.dto.NewsUserDto;
import com.nfa.exception.NewsUserNotFoundException;
import com.nfa.service.NewsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static com.nfa.config.CustomNewsUserDetails.fromNewsUserDtoToCustomUserDetails;

@Component
public class CustomNewsUserDetailsService implements UserDetailsService {

    private NewsUserService newsUserService;

    @Autowired
    CustomNewsUserDetailsService(@Lazy NewsUserService newsUserService) {
        this.newsUserService = newsUserService;
    }

    @Override
    public CustomNewsUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        NewsUserDto newsUserDto;
        try {
            newsUserDto = newsUserService.findByEmail(email);
        } catch (NewsUserNotFoundException ex) {
            throw new UsernameNotFoundException("No user with this email");
        }
        return fromNewsUserDtoToCustomUserDetails(newsUserDto);
    }

}
