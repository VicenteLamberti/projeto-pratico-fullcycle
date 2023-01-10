package com.fullcycle.vicente.domain.category;

import com.fullcycle.vicente.domain.exceptions.DomainException;
import com.fullcycle.vicente.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedActive = true;

        final Category actualCategory = Category.newCategory(expectedName,expectedDescription,expectedActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());

        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldException(){
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final Integer expectedErrorCount = 1;
        final boolean expectedActive = true;
        final String expectedErrorMessage = "'name' should not be null";


        final Category actualCategory = Category.newCategory(expectedName,expectedDescription,expectedActive);

        final DomainException exception = Assertions.assertThrows(DomainException.class, ()->actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

    }
}
