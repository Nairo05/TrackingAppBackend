package de.dhbw.trackingappbackend.dev;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(title = "TrailBlazer Backend", version = "0.1", description = "handles requests from frontends"))
@SecurityScheme(name = "oauth2", type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(clientCredentials  = @OAuthFlow(tokenUrl = "${openapi.oAuthFlow.tokenUrl}", scopes = {
                @OAuthScope(name = "openid", description = "openid scope")
        })))
public class OpenApiConfig {
}
