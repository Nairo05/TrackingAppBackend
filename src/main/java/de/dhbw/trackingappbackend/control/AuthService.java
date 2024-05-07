package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.TokenEntity;
import de.dhbw.trackingappbackend.entity.TokenRepository;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import de.dhbw.trackingappbackend.model.request.RegisterRequest;
import de.dhbw.trackingappbackend.model.response.JwtResponse;
import de.dhbw.trackingappbackend.security.JwtUtils;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public JwtResponse generateJWTResponse() {

        UserDetailsImpl userDetails = getUserFromContext();

        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getId());

        return new JwtResponse(jwt, refreshToken, userDetails.getUsername());
    }

    public UserDetailsImpl emailLogin(String email, String password) {

        Optional<AppUser> appUser = userRepository.findFirstByEmail(email);

        if (appUser.isEmpty()) {
            return null;
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(appUser.get().getUsername(), password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return (UserDetailsImpl) authentication.getPrincipal();
    }

    public void logout(){

        String currentUser = getUserFromContext().getId();

        tokenRepository.deleteAllByUserId(currentUser);

        SecurityContextHolder.clearContext();

    }

    public void registerUser(RegisterRequest signUpRequest) {

        if (signUpRequest.getUsername() == null) {
            signUpRequest.setUsername(signUpRequest.getEmail());
        }

        AppUser appUser = new AppUser(
                UUID.randomUUID().toString(),
                signUpRequest.getFirstname(),
                signUpRequest.getLastname(),
                signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                Collections.emptyList(),
                Collections.emptyList(),
                null
        );

        userRepository.save(appUser);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDetailsImpl getUserFromContext() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserDetailsImpl) authentication.getPrincipal();
    }

    public JwtResponse refreshToken(UUID refreshTokenUuid, String bearer) throws Exception {

        Optional<TokenEntity> token = tokenRepository.findByRefreshToken(refreshTokenUuid.toString());

        if (token.isEmpty()) {
            throw new Exception();
        }

        Optional<AppUser> appUser = userRepository.findById(token.get().getUserId());

        if (appUser.isEmpty()) {
            throw new Exception();
        } else {

            AppUser currentUser = appUser.get();

            String jwt = jwtUtils.generateJwtToken(currentUser.getUsername());
            String refreshToken = jwtUtils.generateRefreshToken(currentUser.getId());

            return new JwtResponse(jwt, refreshToken, currentUser.getUsername());
        }
    }

    public boolean isUserNameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserDetailsImpl userNameLogin(String username, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
