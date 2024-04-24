package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.TokenEntity;
import de.dhbw.trackingappbackend.entity.TokenRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
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

        String jwt = jwtUtils.generateJwtToken(userDetails.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getId());

        return new JwtResponse(jwt, refreshToken, userDetails.getEmail());
    }

    public UserDetailsImpl login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return (UserDetailsImpl) authentication.getPrincipal();
    }

    public void logout(){

        String currentUser = getUserFromContext().getId();

        tokenRepository.deleteAllByUserId(currentUser);

        //TODO ?
        SecurityContextHolder.clearContext();

    }

    public void registerUser(RegisterRequest signUpRequest) {

        AppUser appUser = new AppUser(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        appUser.setFirstname(signUpRequest.getFirstname());
        appUser.setLastname(signUpRequest.getLastname());

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

            String jwt = jwtUtils.generateJwtToken(currentUser.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(currentUser.getId());

            return new JwtResponse(jwt, refreshToken, currentUser.getEmail());
        }
    }
}
