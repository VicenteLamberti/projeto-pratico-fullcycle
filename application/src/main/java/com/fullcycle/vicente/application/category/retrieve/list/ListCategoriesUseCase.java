package com.fullcycle.vicente.application.category.retrieve.list;

import com.fullcycle.vicente.application.UseCase;
import com.fullcycle.vicente.domain.category.CategorySearchQuery;
import com.fullcycle.vicente.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
