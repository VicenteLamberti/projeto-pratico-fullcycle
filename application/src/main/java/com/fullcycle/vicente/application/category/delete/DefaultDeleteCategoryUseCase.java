package com.fullcycle.vicente.application.category.delete;

import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase{
    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway){
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }
    @Override
    public void execute(String anId) {

        categoryGateway.deleteById(CategoryID.from(anId));
    }
}
