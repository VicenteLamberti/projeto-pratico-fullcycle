package com.fullcycle.vicente.infrastructure.category.presenters;

import com.fullcycle.vicente.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.vicente.application.category.retrieve.list.CategoryListOutput;
import com.fullcycle.vicente.infrastructure.category.models.CategoryResponse;
import com.fullcycle.vicente.infrastructure.category.models.CategoryListResponse;

import java.util.function.Function;

public interface CategoryApiPresenter {


    Function<CategoryOutput, CategoryResponse> present =
            output ->
                    new CategoryResponse(
                            output.id().getValue(),
                            output.name(),
                            output.description(),
                            output.isActive(),
                            output.createdAt(),
                            output.updatedAt(),
                            output.deletedAt()
                    );


    //    pode ser assim
    static CategoryResponse present(final CategoryOutput output){
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output){
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }


}
