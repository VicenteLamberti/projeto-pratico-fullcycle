package com.fullcycle.vicente.application.genre.retrieve.list;

import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreID;

import java.time.Instant;

public record GenreListOutput(
        GenreID id,
        String name,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {
    public static GenreListOutput from(Genre aGenre){
        return new GenreListOutput(
                aGenre.getId(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getDeletedAt()
        );
    }
}
