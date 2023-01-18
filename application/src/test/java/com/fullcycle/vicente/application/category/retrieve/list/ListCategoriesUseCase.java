package com.fullcycle.vicente.application.category.retrieve.list;

import com.fullcycle.vicente.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategorySearchQuery;
import com.fullcycle.vicente.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCase {

    @InjectMocks
    DefaultListCategoriesUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidQuery_whenCallListCategories_thenShouldReturnCategories(){
        final List<Category> categories = List.of(Category.newCategory("Filmes","null",true),
                Category.newCategory("Series","null",true));

        final int expectedPage = 0;

        final int expectedPerPage=10;

        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";



        final CategorySearchQuery aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);


        final Pagination expectedPagination = new Pagination<>(expectedPage,expectedPerPage,categories.size(),categories);


        final int expectedItemsCount=2;
        final List<Category> expectedResult = expectedPagination.map(CategoryListOutput::from);
        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount,actualResult.items().size);
        Assertions.assertEquals(expectedResult,actualResult);
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedPage,actualResult.page());
        Assertions.assertEquals(categories.size(),actualResult.total());


    }

    public void givenAValidQuery_whenHasNotResult_thenShouldReturnEmptyCategories(){

    }

    public void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnException(){

    }
}
