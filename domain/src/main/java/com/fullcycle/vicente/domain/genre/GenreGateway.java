package com.fullcycle.vicente.domain.genre;

import com.fullcycle.vicente.domain.pagination.Pagination;
import com.fullcycle.vicente.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {
    Genre create(Genre aGenre);
    void deleteById(GenreID anId);

    Optional<Genre> findById(GenreID anId);

    Genre update(Genre aCategory);

    Pagination<Genre> findAll(SearchQuery aQuery);
}
