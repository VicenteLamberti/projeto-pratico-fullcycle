package com.fullcycle.vicente.application.category.update;

import com.fullcycle.vicente.IntegrationTest;
import com.fullcycle.vicente.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.DomainException;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;


    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory("Film", null, true);

        save(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());
        CategoryID expectedId = aCategory.getId();


        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);


        final UpdateCategoryOutput actualOutput = useCase.execute(aCommand).get();



        Mockito.verify(categoryGateway, Mockito.times(1)).findById(expectedId);
        final CategoryJPAEntity actualCategory = categoryRepository.findById(actualOutput.id().getValue()).get();



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
    public void givenAInvalidName_whenCallUpdateCategory_thenShouldReturnDomainException(){
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedErrorMessage = "'name' should not be null";
        final int expectedErrorCount = 1;

        final Category aCategory = Category.newCategory("Film", null, true);
        save(aCategory);
        Assertions.assertEquals(1,categoryRepository.count());

        CategoryID expectedId = aCategory.getId();

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);


        final Notification notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.first().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Mockito.verify(categoryGateway, Mockito.times(0)).update(Mockito.any());
    }


    @Test
    public void givenAValidInactiveCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive =  false;


        final Category aCategory = Category.newCategory("Film", null, true);
        save(aCategory);
        Assertions.assertEquals(1,categoryRepository.count());



        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());
        CategoryID expectedId = aCategory.getId();
        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        final UpdateCategoryOutput actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());


        Mockito.verify(categoryGateway, Mockito.times(1)).findById(expectedId);
        final CategoryJPAEntity actualCategory = categoryRepository.findById(actualOutput.id().getValue()).get();


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

        final Category aCategory = Category.newCategory("Film", null, true);
        save(aCategory);
        Assertions.assertEquals(1,categoryRepository.count());


        CategoryID expectedId = aCategory.getId();
        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                        .when(categoryGateway).update(Mockito.any());




        final Notification notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.first().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());


        Mockito.verify(categoryGateway, Mockito.times(1)).findById(expectedId);
        final CategoryJPAEntity actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aCategory.getName(),actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(),actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(),actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
//        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }




    @Test
    public void givenAInvalidId_whenGatewayThrowsRandomException_shouldReturnAException(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;
        final String expectedId = "123";
        final String expectedErrorMessage = "Category with ID 123 was not found";
        final int expectedErrorCount = 1;




        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive);


        DomainException actualException = Assertions.assertThrows(DomainException.class, ()-> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(CategoryID.from(expectedId)));
        Mockito.verify(categoryGateway,Mockito.times(0)).update(Mockito.any());

    }

    private void save(final Category... aCategory) {

        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJPAEntity::from)
                        .toList()
        );
    }
}
