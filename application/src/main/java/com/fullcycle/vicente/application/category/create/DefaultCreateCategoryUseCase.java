package com.fullcycle.vicente.application.category.create;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase{


    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand aCommand) {
        final String  aName = aCommand.name();
        final String aDescription = aCommand.description();
        final boolean isActive = aCommand.isActive();

        final Category aCategory = Category.newCategory(aName,aDescription,isActive);
        aCategory.validate(new ThrowsValidationHandler());

        return CreateCategoryOutput.from(this.categoryGateway.create(aCategory));
    }
}
