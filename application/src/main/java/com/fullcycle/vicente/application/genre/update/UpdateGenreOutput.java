package com.fullcycle.vicente.application.genre.update;

import com.fullcycle.vicente.domain.genre.Genre;

public record UpdateGenreOutput(
        String id
) {

    public static UpdateGenreOutput from(final String anID){
        return  new UpdateGenreOutput(anID);
    }

    public static UpdateGenreOutput from(final Genre aGenre){
        return  new UpdateGenreOutput(aGenre.getId().getValue());
    }
}
