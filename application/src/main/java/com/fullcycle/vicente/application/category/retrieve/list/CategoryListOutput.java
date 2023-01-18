package com.fullcycle.vicente.application.category.retrieve.list;

import com.fullcycle.vicente.domain.category.CategoryID;

import java.time.Instant;

public record CategoryListOutput(
        CategoryID id,
        String name,
        String description,

        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
}
