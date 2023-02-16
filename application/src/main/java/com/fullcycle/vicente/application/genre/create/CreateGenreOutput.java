package com.fullcycle.vicente.application.genre.create;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.genre.Genre;

public record CreateGenreOutput(
        String id
) {

    public static CreateGenreOutput from(final String anID){
        return  new CreateGenreOutput(anID);
    }

    public static CreateGenreOutput from(final Genre aGenre){
        return  new CreateGenreOutput(aGenre.getId().getValue());
    }
}
