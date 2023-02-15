package com.fullcycle.vicente.application.category.retrieve.list;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.pagination.SearchQuery;
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
public class ListCategoriesUseCaseTest {

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



        final SearchQuery aQuery = new SearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);


        final Pagination<Category> expectedPagination = new Pagination<Category>(expectedPage,expectedPerPage,categories.size(),categories);


        final int expectedItemsCount=2;
        final Pagination<CategoryListOutput> expectedResult = expectedPagination.map(CategoryListOutput::from);
        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount,actualResult.items().size());
        Assertions.assertEquals(expectedResult,actualResult);
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(categories.size(),actualResult.total());


    }

    @Test
    public void givenAValidQuery_whenHasNotResult_thenShouldReturnEmptyCategories(){

        final List<Category> categories = List.<Category>of();
        final int expectedPage = 0;

        final int expectedPerPage=10;

        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";
        final int expectedItemsCount=0;


        final SearchQuery aQuery = new SearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);


        final Pagination<Category> expectedPagination = new Pagination<Category>(expectedPage,expectedPerPage,categories.size(),categories);



        final Pagination<CategoryListOutput> expectedResult = expectedPagination.map(CategoryListOutput::from);

        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery)))
                .thenReturn(expectedPagination);

        final Pagination<CategoryListOutput> actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount,actualResult.items().size());
        Assertions.assertEquals(expectedResult,actualResult);
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(categories.size(),actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnException(){

        final int expectedPage = 0;

        final int expectedPerPage=10;

        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";
        final String expectedErrorMessage = "Gateway error";


        final SearchQuery aQuery = new SearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);


        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, ()->
                useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());
    }
}
