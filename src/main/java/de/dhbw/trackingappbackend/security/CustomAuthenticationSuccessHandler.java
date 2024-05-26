package de.dhbw.trackingappbackend.security;

import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletSecurityElement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Ssl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        Authentication authenticationNew = new OAuth2AuthenticationToken(principal, principal.getAuthorities(), "clientRegistrationId");

        SecurityContextHolder.getContext().setAuthentication(authenticationNew);

        String username = principal.getAttribute("login").toString();

        System.out.println("xxxx");
        System.out.println(principal);

        if (!userRepository.existsByUsername(username)) {
            AppUser appUser = AppUser
                    .builder()
                    .username(username)
                    .email(username)
                    .password("GITHUB AUTH CREDIT")
                    .build();
            userRepository.save(appUser);
        }

        String token = jwtUtils.generateJwtToken(username);

        response.sendRedirect("/handlesuccess?token="+token);
    }

}
