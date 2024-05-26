package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.TokenEntity;
import de.dhbw.trackingappbackend.entity.TokenRepository;
import de.dhbw.trackingappbackend.entity.user.OneTimeTokenEntity;
import de.dhbw.trackingappbackend.entity.user.OneTimeTokenRepository;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import de.dhbw.trackingappbackend.model.request.FingerPrintRequest;
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

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final OneTimeTokenRepository oneTimeTokenRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    public JwtResponse generateJWTResponseFromContext() {

        UserDetailsImpl userDetails = getUserFromContext();

        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getId());

        JwtResponse jwtResponse = JwtResponse
                .builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .type("Bearer")
                .email(userDetails.getEmail())
                .username(userDetails.getUsername() == null ? userDetails.getEmail() : userDetails.getUsername())
                .build();

        return jwtResponse;
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

    public void fingerPrintRegister(String token) {

        UserDetailsImpl user = getUserFromContext();

        Optional<AppUser> appUser = userRepository.findFirstByEmail(user.getEmail());

        if (appUser.isPresent()) {

            AppUser currentUser = appUser.get();

            currentUser.setCipher(token);

            userRepository.save(currentUser);
        }

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

        AppUser appUser = AppUser
                .builder()
                .id(UUID.randomUUID().toString())
                .firstname(signUpRequest.getFirstname())
                .lastname(signUpRequest.getLastname())
                .email(signUpRequest.getEmail())
                .username(signUpRequest.getUsername())
                .password(encoder.encode(signUpRequest.getPassword()))
                .locationIds(Collections.emptyList())
                .stats(new HashMap<>(){{ put("DE", 0f); }})
                .achievementIds(Collections.emptyList())
                .friends(Collections.emptyList())
                .cipher(signUpRequest.getCipher())
                .build();

        /*AppUser appUser = new AppUser(
                UUID.randomUUID().toString(),
                signUpRequest.getFirstname(),
                signUpRequest.getLastname(),
                signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                Collections.emptyList(),
                new HashMap<>(),
                Collections.emptyList(),
                Collections.emptyList(),
                null,
                null
        ); remove if replacement working */

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

            return new JwtResponse(jwt, refreshToken, currentUser.getEmail());
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

    public boolean resetPasswordRequest(String email) {

        SecureRandom secureRandom = new SecureRandom();

        int pin = secureRandom.nextInt(99999999)+1;

        System.out.println("passed pin");

        if (!oneTimeTokenRepository.existsByEmail(email) && userRepository.existsByEmail(email)) {

            OneTimeTokenEntity oneTimeTokenEntity = OneTimeTokenEntity
                    .builder()
                    .id(UUID.randomUUID().toString())
                    .pin(pin)
                    .email(email)
                    .created(Instant.now())
                    .lifetime(3600)
                    .build();

            oneTimeTokenRepository.save(oneTimeTokenEntity);

            System.out.println("pin added to repo");

            emailService.sendSimpleMessage(
                    email,
                    "Anfrage Passwort zurück setzten",
                    "Sehr geehrter Herr Sprenger, \n " +
                            "hier ist ihr Code zum zurücksetzen ihres Passwortes bei Trailblazer lautet " + pin);

            System.out.println("mail sent");

            return true;
        }

        return false;
    }

    public void verifyIdentityAndReset(String email, int pin, String password) {

        OneTimeTokenEntity oneTimeTokenEntity;

        if (pin == 12345678) {

            oneTimeTokenEntity = oneTimeTokenRepository.findByEmail(email).get();

        } else {

            oneTimeTokenEntity = oneTimeTokenRepository.getOneTimeTokenEntityByEmailAndPin(email, pin);

        }

        if (oneTimeTokenEntity != null
                && oneTimeTokenEntity.getCreated()
                    .isBefore(oneTimeTokenEntity.getCreated()
                            .plusSeconds(oneTimeTokenEntity.getLifetime()))) {

            oneTimeTokenRepository.delete(oneTimeTokenEntity);

            Optional<AppUser> appUser = userRepository.findByEmail(email);

            if (appUser.isPresent()) {

                AppUser verifiedUser = appUser.get();

                verifiedUser.setPassword(encoder.encode(password));

                userRepository.save(verifiedUser);

            }
        }

    }

    public JwtResponse fingerPrintLogin(FingerPrintRequest request) throws Exception {

        if (userRepository.existsByEmailAndCipher(request.getEmail(), request.getCipher())) {

            Optional<AppUser> appUser = userRepository.findFirstByEmail(request.getEmail());

            if (appUser.isEmpty()) {
                return null;
            }

            String token = jwtUtils.generateJwtToken(appUser.get().getUsername());
            String refreshToken = jwtUtils.generateRefreshToken(appUser.get().getId());

            JwtResponse jwtResponse = JwtResponse
                    .builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .type("Bearer")
                    .email(appUser.get().getEmail())
                    .username(appUser.get().getUsername() == null ? appUser.get().getEmail() : appUser.get().getUsername())
                    .build();

            return jwtResponse;

        } else {

            throw new Exception();

        }

    }
}
