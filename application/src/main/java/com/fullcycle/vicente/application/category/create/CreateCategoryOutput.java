package com.fullcycle.vicente.application.category.create;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryID;

public record CreateCategoryOutput(
        CategoryID id
) {
    public static CreateCategoryOutput from(final Category aCategory){
        return  new CreateCategoryOutput(aCategory.getId());
    }
}
