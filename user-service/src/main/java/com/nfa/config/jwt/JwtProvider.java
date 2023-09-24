package com.nfa.config.jwt;

import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
@Log
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(String email) {
        Date date = Date.from(LocalDate.now().plusDays(45).atStartOfDay(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (UnsupportedJwtException ex) {
            log.severe("unsupported");
        } catch (MalformedJwtException ex) {
            log.severe("token is malformed");
        } catch (ExpiredJwtException ex) {
            log.severe("token expired");
        } catch (SignatureException ex) {
            log.severe("token error in signature");
        } catch (IllegalArgumentException ex) {
            log.severe("token error: illegal args");
        }
        return false;
    }

    public String getEmailFromToken(String jwt) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody();
        return claims.getSubject();
    }

}
