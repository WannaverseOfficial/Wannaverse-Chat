package com.wannaverse.users.persistence;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user_groups_t")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String groupName;

    @ElementCollection private List<String> members;
}
