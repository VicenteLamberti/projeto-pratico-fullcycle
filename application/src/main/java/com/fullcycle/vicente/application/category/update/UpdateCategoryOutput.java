package com.fullcycle.vicente.application.category.update;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryID;

public record UpdateCategoryOutput(
        String id
) {

    public static  UpdateCategoryOutput from(final String anId){
        return new UpdateCategoryOutput(anId);
    }

    public static  UpdateCategoryOutput from(final Category aCategory){
        return new UpdateCategoryOutput(aCategory.getId().getValue());
    }
}
