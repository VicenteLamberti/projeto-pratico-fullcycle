package com.fullcycle.vicente.application.genre.retrieve.list;

import com.fullcycle.vicente.application.UseCase;
import com.fullcycle.vicente.domain.pagination.Pagination;
import com.fullcycle.vicente.domain.pagination.SearchQuery;

import java.util.List;

public abstract class ListGenresUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
