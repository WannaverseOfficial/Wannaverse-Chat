package com.wannaverse.users.persistence;

import com.wannaverse.users.dto.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users_t")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_address", nullable = false, unique = true)
    @NotBlank
    @Email
    private String emailAddress;

    @Column(name = "first_name", nullable = false)
    @NotBlank
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    private String lastName;

    @Column(name = "display_name", nullable = false)
    @NotBlank
    private String displayName;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 8)
    @ToString.Exclude
    private String password;

    public UserDTO toUserDTO() {
        return new UserDTO(id, firstName, lastName, displayName);
    }
}
