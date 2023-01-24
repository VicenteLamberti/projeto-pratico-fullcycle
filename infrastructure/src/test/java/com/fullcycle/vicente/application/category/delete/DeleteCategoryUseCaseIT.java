package com.fullcycle.vicente.application.category.delete;

import com.fullcycle.vicente.IntegrationTest;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;


    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk(){
        final Category aCategory = Category.newCategory("Filmes","A categoria mais assistida", true);
        final CategoryID aExpectedId = aCategory.getId();
        Assertions.assertEquals(0,categoryRepository.count());
        save(aCategory);
        Assertions.assertEquals(1,categoryRepository.count());
        deleteCategoryUseCase.execute(aExpectedId.getValue());
        Assertions.assertEquals(0,categoryRepository.count());


    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOk(){
        final CategoryID expectedId = CategoryID.from("123");
        final Category aCategory = Category.newCategory("Filmes","A categoria mais assistida", true);
        Assertions.assertEquals(0,categoryRepository.count());
        save(aCategory);
        Assertions.assertEquals(1,categoryRepository.count());
        deleteCategoryUseCase.execute(expectedId.getValue());
        Assertions.assertEquals(1,categoryRepository.count());
    }

    @Test
    public void givenAValid_whenGatewayThrowsException_shouldReturnException(){
        final Category aCategory = Category.newCategory("Filmes","A categoria mais assistida", true);
        final CategoryID expectedId = aCategory.getId();
        Mockito.doThrow(new IllegalStateException("Gatewat error"))
                .when(categoryGateway).deleteById(expectedId);
        Assertions.assertThrows(IllegalStateException.class, ()->deleteCategoryUseCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway,Mockito.times(1)).deleteById(expectedId);
    }




    private void save(final Category... aCategory) {

        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJPAEntity::from)
                        .toList()
        );
    }
}
