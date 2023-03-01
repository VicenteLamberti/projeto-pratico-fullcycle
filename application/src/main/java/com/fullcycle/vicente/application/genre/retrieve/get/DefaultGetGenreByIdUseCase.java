package com.fullcycle.vicente.application.genre.retrieve.get;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.NotFoundException;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreGateway;
import com.fullcycle.vicente.domain.genre.GenreID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase{

    private final GenreGateway gateway;

    public DefaultGetGenreByIdUseCase(final GenreGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public GenreOutput execute(String anId) {
        GenreID genreId = GenreID.from(anId);
        return gateway.findById(genreId)
                .map(GenreOutput::from)
                .orElseThrow(notFound(genreId));
    }

    private Supplier<NotFoundException> notFound(GenreID anId){
        return ()-> NotFoundException.with(
                Genre.class,anId
        );
    }
}
