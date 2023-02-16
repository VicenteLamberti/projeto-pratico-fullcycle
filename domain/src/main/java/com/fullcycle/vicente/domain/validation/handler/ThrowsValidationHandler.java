package com.fullcycle.vicente.domain.validation.handler;

import com.fullcycle.vicente.domain.exceptions.DomainException;
import com.fullcycle.vicente.domain.validation.Error;
import com.fullcycle.vicente.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(ValidationHandler anHandler) {

        throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public <T> T validate(Validation<T> aValidation) {
        try{
            return aValidation.validate();
        }
        catch (final Exception ex){
            throw DomainException.with(new Error(ex.getMessage()));
        }
    }

    @Override
    public List<Error> getErrors() {
        return null;
    }
}
