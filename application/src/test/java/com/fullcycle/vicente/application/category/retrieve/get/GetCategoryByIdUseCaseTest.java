package com.fullcycle.vicente.application.category.retrieve.get;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    DefaultGetCategoryByIdUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final Category aCategory = Category.newCategory(expectedName,expectedDescription, expectedIsActive);
        final CategoryID aExpectedId = aCategory.getId();

        Mockito.when(categoryGateway.findById(aExpectedId))
                        .thenReturn(Optional.of(aCategory.clone()));

        final CategoryOutput actualCategory =  useCase.execute(aExpectedId.getValue());


        Assertions.assertEquals(expectedName,actualCategory.name());
        Assertions.assertEquals(expectedDescription,actualCategory.description());
        Assertions.assertEquals(aExpectedId,actualCategory.id());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertEquals(CategoryOutput.from(aCategory),actualCategory);
        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),actualCategory.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.deletedAt());
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound(){
        final String expectedErrorMessage = "Category with ID 123 was not found";
        final CategoryID aExpectedId = CategoryID.from("123");

        Mockito.when(categoryGateway.findById(aExpectedId))
                .thenReturn(Optional.empty());

        final DomainException actualException =
                Assertions.assertThrows(DomainException.class, () -> useCase.execute(aExpectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());
    }


    @Test
    public void givenAValid_whenGatewayThrowsException_shouldReturnException(){
        final String expectedErrorMessage = "Gateway error";

        final CategoryID expectedId = CategoryID.from("123");


        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).findById(Mockito.eq(expectedId));
        final IllegalStateException actualException =
                Assertions
                        .assertThrows(IllegalStateException.class,
                                ()->useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    }




}
