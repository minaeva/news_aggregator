package com.nfa.config.jwt;

import static org.springframework.util.StringUtils.hasText;

public class JwtHelper {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final int BEARER_LETTERS_QUANTITY = 7;

    public static String getJwtFromString(String bearer) {
        if (hasText(bearer) && bearer.startsWith(BEARER)) {
            return bearer.substring(BEARER_LETTERS_QUANTITY);
        }
        return null;
    }

}
