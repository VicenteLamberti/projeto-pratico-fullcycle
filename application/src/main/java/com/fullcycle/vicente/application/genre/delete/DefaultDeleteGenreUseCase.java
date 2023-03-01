package com.fullcycle.vicente.application.genre.delete;

import com.fullcycle.vicente.domain.genre.GenreGateway;
import com.fullcycle.vicente.domain.genre.GenreID;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase{
    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(String anId) {
        this.genreGateway.deleteById(GenreID.from(anId));
    }
}
