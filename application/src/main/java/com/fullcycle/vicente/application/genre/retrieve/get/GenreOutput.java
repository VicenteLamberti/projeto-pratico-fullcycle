package com.fullcycle.vicente.application.genre.retrieve.get;

import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreID;

import java.time.Instant;
import java.util.List;

public record GenreOutput(
        GenreID id,
        String name,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt,

        List<String> categories
) {

    public static GenreOutput from(Genre aGenre){
        return new GenreOutput(aGenre.getId(),aGenre.getName(),aGenre.isActive(),aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),aGenre.getDeletedAt(),aGenre.getCategories().stream().map(CategoryID::getValue).toList());
    }
}
