package com.wannaverse.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.wannaverse.users.persistence.elastic")
@EnableJpaRepositories(basePackages = "com.wannaverse.users.persistence.jpa")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }

    @Bean
    public Argon2PasswordEncoder getArgon2PasswordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 1, 65536, 3);
    }
}
