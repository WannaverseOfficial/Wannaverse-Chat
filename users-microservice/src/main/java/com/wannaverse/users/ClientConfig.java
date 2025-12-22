package com.wannaverse.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

@Configuration
public class ClientConfig {
    private final Argon2PasswordEncoder passwordEncoder;
    private final ServerPortProvider portProvider;

    @Autowired
    public ClientConfig(Argon2PasswordEncoder passwordEncoder, ServerPortProvider portProvider) {
        this.passwordEncoder = passwordEncoder;
        this.portProvider = portProvider;
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        String hash = passwordEncoder.encode("secret");

        RegisteredClient client =
                RegisteredClient.withId("wannaverse-chat")
                        .clientId("wannaverse-chat")
                        .clientSecret(hash)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri(
                                "http://localhost:%d/callback".formatted(portProvider.getPort()))
                        .scope("read")
                        .clientSettings(
                                ClientSettings.builder()
                                        .requireProofKey(false)
                                        .requireAuthorizationConsent(false)
                                        .build())
                        .build();

        return new InMemoryRegisteredClientRepository(client);
    }
}
