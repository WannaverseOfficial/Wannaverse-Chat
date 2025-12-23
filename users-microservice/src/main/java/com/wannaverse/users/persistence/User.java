package com.wannaverse.users.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users_t")
public class User {
    @Id private String id;

    @Column private Long creationDate;

    @Column private String firstName;

    @Column private String lastName;

    @Column private String displayName;

    @Column private String about;
}
