package com.wannaverse.authorization.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user_credentials_t")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_address", nullable = false, unique = true)
    @NotBlank
    @Email
    private String emailAddress;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 8)
    @ToString.Exclude
    private String password;
}
