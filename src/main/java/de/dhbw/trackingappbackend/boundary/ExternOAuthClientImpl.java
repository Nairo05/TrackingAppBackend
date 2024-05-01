package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import org.apache.tomcat.util.log.SystemLogHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class ExternOAuthClientImpl {

    @GetMapping("/login/oauth2/code/github")
    public String handleGitHubOAuth2Callback(OAuth2AuthenticationToken token) {

        System.out.println(token);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication.toString());

        return "OAuth2 authentication successful";
    }

}
