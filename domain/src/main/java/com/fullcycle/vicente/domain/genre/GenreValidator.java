package com.fullcycle.vicente.domain.genre;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.validation.Error;
import com.fullcycle.vicente.domain.validation.ValidationHandler;
import com.fullcycle.vicente.domain.validation.Validator;

public class GenreValidator extends Validator {

    public static final int MAX_NAME_LENGTH = 255;
    private final Genre genre;
    public GenreValidator(final Genre aGenre, final ValidationHandler aHandler) {
        super(aHandler);
        this.genre = aGenre;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        String name = this.genre.getName();
        if( name == null){
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if(length > MAX_NAME_LENGTH || length < 1){
            this.validationHandler().append(new Error("'name' should  be  than 1 character and 255 character"));
            return;
        }
    }
}
