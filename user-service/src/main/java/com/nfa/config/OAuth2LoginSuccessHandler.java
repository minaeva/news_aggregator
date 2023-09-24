package com.nfa.config;

import com.nfa.dto.NewsUserDto;
import com.nfa.entity.NewsUserRole;
import com.nfa.entity.RegistrationSource;
import com.nfa.exception.NewsUserNotFoundException;
import com.nfa.service.NewsUserService;
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

    private final NewsUserService newsUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if (RegistrationSource.GOOGLE.name()
                .equalsIgnoreCase(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String name = attributes.getOrDefault("name", "").toString();
            String email = attributes.getOrDefault("email", "").toString();

            NewsUserDto newsUserDto = new NewsUserDto();
            try {
                newsUserDto = newsUserService.findByEmail(email);
                DefaultOAuth2User newUser = new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(newsUserDto.getRole().name())),
                        attributes, "id");
                Authentication securityAuth = new OAuth2AuthenticationToken(newUser, List.of(new SimpleGrantedAuthority(newsUserDto.getRole().name())),
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
            } catch (NewsUserNotFoundException e) {
                newsUserDto.setName(name);
                newsUserDto.setEmail(email);
                newsUserDto.setRole(NewsUserRole.NEWS_USER_ROLE);
                newsUserDto.setRegistrationSource(RegistrationSource.GOOGLE);
                newsUserService.save(newsUserDto);

                DefaultOAuth2User newUser = new DefaultOAuth2User(
                        List.of(new SimpleGrantedAuthority(newsUserDto.getRole().name())),
                        attributes, "id");
                Authentication securityAuth = new OAuth2AuthenticationToken(newUser,
                        List.of(new SimpleGrantedAuthority(newsUserDto.getRole().name())),
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}