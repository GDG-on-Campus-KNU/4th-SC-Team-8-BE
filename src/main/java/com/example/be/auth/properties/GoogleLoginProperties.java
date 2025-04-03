package com.example.be.auth.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth2.client.registration.google")
@RequiredArgsConstructor
@Getter
@Setter
public class GoogleLoginProperties {
    private String clientId;
    private String clientSecret;
    private String scope;
    private String tokenURI;
    private String profileURI;
}
