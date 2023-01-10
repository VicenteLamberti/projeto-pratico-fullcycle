package com.fullcycle.vicente.domain.category;

import com.fullcycle.vicente.domain.validation.Error;
import com.fullcycle.vicente.domain.validation.ValidationHandler;
import com.fullcycle.vicente.domain.validation.Validator;

public class CategoryValidator extends Validator {

    public static final int MAX_NAME_LENGTH = 255;
    private final Category category;
    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        checkNameConstraints();

        if(this.category.getName().length() < 3){
            this.validationHandler().append(new Error("'name' should not be less than 3 character"));
        }

    }

    private void checkNameConstraints() {
        String name = this.category.getName();
        if( name == null){
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if(length > MAX_NAME_LENGTH || length < 3){
            this.validationHandler().append(new Error("'name' should  be  than 3 character and 255 character"));
            return;
        }
    }
}
