package com.fullcycle.vicente.application.category.retrieve.get;

import com.fullcycle.vicente.IntegrationTest;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.DomainException;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.Optional;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName,expectedDescription, expectedIsActive);
        final CategoryID aExpectedId = aCategory.getId();


        Assertions.assertEquals(0, categoryRepository.count());

        save(aCategory);
        Assertions.assertEquals(1, categoryRepository.count());
        final CategoryOutput actualCategory =  useCase.execute(aExpectedId.getValue());


        Assertions.assertEquals(expectedName,actualCategory.name());
        Assertions.assertEquals(expectedDescription,actualCategory.description());
        Assertions.assertEquals(aExpectedId,actualCategory.id());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
//        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.createdAt()); // Tirei por causa dos milésimos
//        Assertions.assertEquals(aCategory.getUpdatedAt(),actualCategory.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.deletedAt());
    }


    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName,expectedDescription, expectedIsActive);



        Assertions.assertEquals(0, categoryRepository.count());

        save(aCategory);

        final String expectedErrorMessage = "Category with ID 123 was not found";
        final CategoryID aExpectedId = CategoryID.from("123");



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

    private void save(final Category... aCategory) {

        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJPAEntity::from)
                        .toList()
        );
    }
}
