package com.energiai.backend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecretString;

    @Value("${jwt.expiration-ms:86400000}")
    private long jwtExpirationInMs;

    private Key jwtSecret;

    @PostConstruct
    public void init() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecretString);
            this.jwtSecret = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            byte[] keyBytes = jwtSecretString.getBytes(StandardCharsets.UTF_8);
            this.jwtSecret = Keys.hmacShaKeyFor(keyBytes);
        }
    }

    public String generarToken(String email) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(fechaExpiracion)
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    public String obtenerEmailDelToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
