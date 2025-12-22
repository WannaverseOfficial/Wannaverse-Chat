package com.wannaverse.users.dto;

import java.util.List;

public record SearchResults<T>(List<T> results, int page, int totalPages) {}
