package com.fullcycle.vicente.application.category.retrieve.list;

import com.fullcycle.vicente.application.UseCase;
import com.fullcycle.vicente.domain.pagination.SearchQuery;
import com.fullcycle.vicente.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
