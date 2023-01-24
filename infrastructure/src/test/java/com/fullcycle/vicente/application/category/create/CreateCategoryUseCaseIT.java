package com.fullcycle.vicente.application.category.create;

import com.fullcycle.vicente.IntegrationTest;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        Assertions.assertEquals(0,repository.count());

        final var actualOutput =  useCase.execute(aCommand).get();

        final CategoryJPAEntity actualCategory = repository.findById(actualOutput.id().getValue()).get();

        Assertions.assertEquals(1,repository.count());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }



    @Test
    public void givenAInvalidName_whenCallCreateCategory_thenShouldReturnDomainException(){
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedErrorMessage = "'name' should not be null";
        final int expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        final Notification notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.first().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(0,repository.count());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());

    }



    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        Assertions.assertEquals(0,repository.count());


        final CreateCategoryOutput actualOutput =  useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Assertions.assertEquals(1,repository.count());

        CategoryJPAEntity actualCategory = repository.findById(actualOutput.id().getValue()).get();

        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());


    }




    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedErrorMessage = "Gateway Error";
        final int expectedErrorCount = 1;

//Assim não funciona porque estou usando o SpyBean
//        Mockito.when(gateway.create(Mockito.any()))
//                .thenThrow(new IllegalStateException(expectedErrorMessage));



        Assertions.assertEquals(0,repository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName,expectedDescription,expectedIsActive);

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(gateway).create(Mockito.any());


        final Notification notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(0,repository.count());
        Assertions.assertEquals(expectedErrorMessage, notification.first().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        Assertions.assertEquals(expectedErrorMessage, notification.first().message());


    }
}
