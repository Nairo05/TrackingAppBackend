package de.dhbw.trackingappbackend.security;

import java.security.Key;
import java.time.Instant;
import java.util.*;

import de.dhbw.trackingappbackend.entity.TokenEntity;
import de.dhbw.trackingappbackend.entity.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {

    private final TokenRepository tokenRepository;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        //TODO refactor deprecated
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJwtToken(String username) {

        //TODO refactor deprecated
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJwtToken(UserDetails userDetails) {

        //TODO refactor deprecated
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String generateRefreshToken(String userID) {
        List<TokenEntity> refreshTokenEntities = tokenRepository.findRefreshTokenEntityByUserId(userID);

        Optional<TokenEntity> earliestExpiringToken = refreshTokenEntities.stream()
                .min(Comparator.comparing(TokenEntity::getExpiryDate));

        if (earliestExpiringToken.isPresent() && refreshTokenEntities.size() >= 3) {
            TokenEntity token = earliestExpiringToken.get();
            tokenRepository.delete(token);
        }

        String refreshToken = UUID.randomUUID().toString();

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserId(userID);
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiryDate(Instant.now().plusSeconds(100000));
        tokenRepository.save(tokenEntity);

        return refreshToken;
    }
}