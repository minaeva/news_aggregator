package com.nfa.config;

import com.nfa.dto.ReaderDto;
import com.nfa.entity.ReaderRole;
import com.nfa.entity.RegistrationSource;
import com.nfa.exception.ReaderNotFoundException;
import com.nfa.service.ReaderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ReaderService readerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if (RegistrationSource.GOOGLE.name()
                .equalsIgnoreCase(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String name = attributes.getOrDefault("name", "").toString();
            String email = attributes.getOrDefault("email", "").toString();

            ReaderDto readerDto = new ReaderDto();
            try {
                readerDto = readerService.findByEmail(email);
                DefaultOAuth2User newUser = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(readerDto.getRole().name())),
                        attributes, "id");
                Authentication securityAuth = new OAuth2AuthenticationToken(newUser, List.of(new SimpleGrantedAuthority(readerDto.getRole().name())),
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
            } catch (ReaderNotFoundException e) {
                readerDto.setName(name);
                readerDto.setEmail(email);
                readerDto.setRole(ReaderRole.READER_ROLE);
                readerDto.setRegistrationSource(RegistrationSource.GOOGLE);
                readerService.save(readerDto);

                DefaultOAuth2User newUser = new DefaultOAuth2User(
                        List.of(new SimpleGrantedAuthority(readerDto.getRole().name())),
                        attributes, "id");
                Authentication securityAuth = new OAuth2AuthenticationToken(newUser,
                        List.of(new SimpleGrantedAuthority(readerDto.getRole().name())),
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}