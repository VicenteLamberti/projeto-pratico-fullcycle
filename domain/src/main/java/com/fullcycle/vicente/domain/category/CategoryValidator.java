package com.fullcycle.vicente.domain.category;

import com.fullcycle.vicente.domain.validation.Error;
import com.fullcycle.vicente.domain.validation.ValidationHandler;
import com.fullcycle.vicente.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private final Category category;
    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        if(this.category.getName() == null){
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }
}
