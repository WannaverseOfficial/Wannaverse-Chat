package com.wannaverse.users.persistence;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user_groups")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column private String groupName;

    @ElementCollection private List<String> members;
}
