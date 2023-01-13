package com.fullcycle.vicente.application.category.update;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryID;

public record UpdateCategoryOutput(
        CategoryID id
) {

    public static  UpdateCategoryOutput from(final Category aCategory){
        return new UpdateCategoryOutput(aCategory.getId());
    }
}
