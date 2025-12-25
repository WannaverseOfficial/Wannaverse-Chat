package com.wannaverse.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
@Entity
@Table(name = "channels_t")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column private Long creationDate;

    @Column(nullable = false)
    private String name;

    @Column private String ownerId;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ElementCollection private List<String> users;

    @ElementCollection
    @CollectionTable(name = "channel_permissions", joinColumns = @JoinColumn(name = "channel_id"))
    @MapKeyColumn(name = "permission_key")
    @Column(name = "permission_value")
    private Map<String, String> permissions;
}
