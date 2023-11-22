package com.nfa.config;

import com.nfa.dto.ReaderDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomNewsUserDetails implements UserDetails {

    public static final String NEWS_USER = "NEWS_USER";
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static CustomNewsUserDetails fromNewsUserDtoToCustomUserDetails(ReaderDto readerDto){
        CustomNewsUserDetails details = new CustomNewsUserDetails();
        details.email = readerDto.getEmail();
        details.password = readerDto.getPassword();
        details.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(NEWS_USER));
        return details;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
