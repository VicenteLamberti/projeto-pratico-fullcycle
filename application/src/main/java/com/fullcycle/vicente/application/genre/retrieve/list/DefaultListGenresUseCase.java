package com.fullcycle.vicente.application.genre.retrieve.list;

import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.genre.GenreGateway;
import com.fullcycle.vicente.domain.pagination.Pagination;
import com.fullcycle.vicente.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Objects;

public class DefaultListGenresUseCase extends ListGenresUseCase{
    private final GenreGateway genreGateway;

    public DefaultListGenresUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(SearchQuery query) {
        return this.genreGateway.findAll(query)
                .map(GenreListOutput::from);
    }
}
