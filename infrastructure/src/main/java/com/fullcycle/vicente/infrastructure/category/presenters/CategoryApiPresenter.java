package com.fullcycle.vicente.infrastructure.category.presenters;

import com.fullcycle.vicente.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.vicente.infrastructure.category.models.CategoryApiOutput;

import java.util.function.Function;

public interface CategoryApiPresenter {


    Function<CategoryOutput, CategoryApiOutput> present =
            output ->
                    new CategoryApiOutput(
                            output.id().getValue(),
                            output.name(),
                            output.description(),
                            output.isActive(),
                            output.createdAt(),
                            output.updatedAt(),
                            output.deletedAt()
                    );


    //    pode ser assim
    static CategoryApiOutput present(final CategoryOutput output){
        return new CategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }


}
