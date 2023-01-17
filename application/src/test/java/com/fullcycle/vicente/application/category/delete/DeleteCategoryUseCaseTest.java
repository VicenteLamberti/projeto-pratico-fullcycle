package com.fullcycle.vicente.application.category.delete;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {
    @InjectMocks
    DefaultDeleteCategoryUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk(){
        final Category aCategory = Category.newCategory("Filmes","A categoria mais assistida", true);
        final CategoryID aExpectedId = aCategory.getId();

        Mockito.doNothing()
                .when(categoryGateway).deleteById(aExpectedId);
        Assertions.assertDoesNotThrow(()->useCase.execute(aExpectedId.getValue()));

        Mockito.verify(categoryGateway,Mockito.times(1)).deleteById(aExpectedId);
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOk(){
        final CategoryID expectedId = CategoryID.from("123");

        Mockito.doNothing()
                .when(categoryGateway).deleteById(expectedId);
        Assertions.assertDoesNotThrow(()->useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway,Mockito.times(1)).deleteById(expectedId);

    }

    @Test
    public void givenAValid_whenGatewayThrowsException_shouldReturnException(){
        final Category aCategory = Category.newCategory("Filmes","A categoria mais assistida", true);
        final CategoryID expectedId = aCategory.getId();


        Mockito.doThrow(new IllegalStateException("Gatewat error"))
                .when(categoryGateway).deleteById(expectedId);
        Assertions.assertThrows(IllegalStateException.class, ()->useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway,Mockito.times(1)).deleteById(expectedId);
    }



}
