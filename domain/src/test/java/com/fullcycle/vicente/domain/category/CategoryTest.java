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
    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldException(){
        final String expectedName = " ";
        final String expectedDescription = "A categoria mais assistida";
        final Integer expectedErrorCount = 1;
        final boolean expectedActive = true;
        final String expectedErrorMessage = "'name' should not be empty";


        final Category actualCategory = Category.newCategory(expectedName,expectedDescription,expectedActive);

        final DomainException exception = Assertions.assertThrows(DomainException.class, ()->actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

    }

    @Test
    public void givenAnInvalidNameLenghtLessThan3_whenCallNewCategoryAndValidate_thenShouldException(){
        final String expectedName = "Te ";
        final String expectedDescription = "A categoria mais assistida";
        final Integer expectedErrorCount = 1;
        final boolean expectedActive = true;
        final String expectedErrorMessage = "'name' should  be  than 3 character and 255 character";


        final Category actualCategory = Category.newCategory(expectedName,expectedDescription,expectedActive);

        final DomainException exception = Assertions.assertThrows(DomainException.class, ()->actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

    }

    @Test
    public void givenAnInvalidNameLenghtMoreThan255Character_whenCallNewCategoryAndValidate_thenShouldException(){
        final String expectedName = """
                          Não obstante, a execução dos pontos do programa promove a alavancagem do 
                          levantamento das variáveis envolvidas. Podemos já vislumbrar o modo pelo qual a 
                          expansão dos mercados mundiais apresenta tendências no sentido de aprovar a manutenção dos níveis de 
                          motivação departamental. A certificação de metodologias que nos auxiliam a lidar com o desenvolvimento 
                          contínuo de distintas formas de atuação acarreta um processo de reformulação e modernização das condições 
                          inegavelmente apropriadas. No mundo atual, a estrutura atual da organização estende o alcance e a 
                          importância dos modos de operação convencionais. A prática 
                          cotidiana prova que o novo modelo estrutural aqui preconizado representa uma abertura 
                          para a melhoria do orçamento setorial.
                """;
        final String expectedDescription = "A categoria mais assistida";
        final Integer expectedErrorCount = 1;
        final boolean expectedActive = true;
        final String expectedErrorMessage = "'name' should  be  than 3 character and 255 character";


        final Category actualCategory = Category.newCategory(expectedName,expectedDescription,expectedActive);

        final DomainException exception = Assertions.assertThrows(DomainException.class, ()->actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());

    }

    @Test
    public void givenAInvalidParamsDescriptionEmpty_whenCallNewCategory_thenInstantiateACategory(){
        final String expectedName = "Filmes";
        final String expectedDescription = " ";
        final boolean expectedActive = true;

        final Category actualCategory = Category.newCategory(expectedName,expectedDescription,expectedActive);

        Assertions.assertDoesNotThrow(()-> actualCategory.validate(new ThrowsValidationHandler()));

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
    public void givenAInvalidParamsFalseIsActive_whenCallNewCategory_thenInstantiateACategory(){
        final String expectedName = "Filmes";
        final String expectedDescription = "Categoria mais assistida";
        final boolean expectedActive = false;

        final Category actualCategory = Category.newCategory(expectedName,expectedDescription,expectedActive);

        Assertions.assertDoesNotThrow(()-> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());

        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }
}
