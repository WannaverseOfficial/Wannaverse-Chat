package com.wannaverse.persistence;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import java.util.List;

@Embeddable
public class Permission {
    @ElementCollection private List<String> values;
}
