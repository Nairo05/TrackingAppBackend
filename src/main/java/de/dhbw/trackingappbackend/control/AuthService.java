package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.TokenEntity;
import de.dhbw.trackingappbackend.entity.TokenRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
import de.dhbw.trackingappbackend.model.request.RegisterRequest;
import de.dhbw.trackingappbackend.model.response.JwtResponse;
import de.dhbw.trackingappbackend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.Token;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public JwtResponse generateJWTResponse(String userId, String email) {

        List<TokenEntity> refreshTokenEntities = tokenRepository.findRefreshTokenEntityByUserId(userId);

        Optional<TokenEntity> earliestExpiringToken = refreshTokenEntities.stream()
                .min(Comparator.comparing(TokenEntity::getExpiryDate));

        if (earliestExpiringToken.isPresent() && refreshTokenEntities.size() >= 3) {
            TokenEntity token = earliestExpiringToken.get();
            tokenRepository.delete(token);
        }

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setRefreshToken(UUID.randomUUID().toString());
        tokenEntity.setExpiryDate(Instant.now().plusSeconds(100000));

        tokenRepository.save(tokenEntity);

        String jwt = jwtUtils.generateJwtToken(userId);
        String refreshToken = tokenEntity.getRefreshToken();

        return new JwtResponse(jwt, refreshToken, email);
    }

    public void logout(String userId){

        tokenRepository.deleteAllByUserId(userId);

        //TODO ?
        SecurityContextHolder.clearContext();

    }

    public void registerUser(RegisterRequest signUpRequest) {

        AppUser appUser = new AppUser(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        userRepository.save(appUser);
    }
}
