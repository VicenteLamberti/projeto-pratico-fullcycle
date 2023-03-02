package com.fullcycle.vicente.application.genre.retrieve.list;

import com.fullcycle.vicente.application.UseCaseTest;
import com.fullcycle.vicente.application.category.retrieve.list.CategoryListOutput;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreGateway;
import com.fullcycle.vicente.domain.pagination.Pagination;
import com.fullcycle.vicente.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListGenresUseCaseTest  extends UseCaseTest {

    @InjectMocks
    DefaultListGenresUseCase useCase;
    @Mock
    GenreGateway genreGateway;
    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListGenres_thenShouldReturnGenres(){
        final List<Genre> genres = List.of(Genre.newGenre("Drama", true), Genre.newGenre("Comédia",false));

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";

        final SearchQuery aQuery = new SearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);

        final Pagination<Genre> expectedPagination = new Pagination<>(expectedPage,expectedPerPage,genres.size(),genres);

        final int expectedItemsCount = 2;

        final Pagination<GenreListOutput> expectedResult = expectedPagination.map(GenreListOutput::from);

        Mockito.when(genreGateway.findAll(Mockito.eq(aQuery)))
                .thenReturn(expectedPagination);



        final Pagination<GenreListOutput> actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount,actualResult.items().size());
        Assertions.assertEquals(expectedResult,actualResult);
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(genres.size(),actualResult.total());

    }


    @Test
    public void givenAValidQuery_whenHasNotResult_thenShouldReturnEmptyGenres(){

        final List<Genre> genres = List.<Genre>of();
        final int expectedPage = 0;

        final int expectedPerPage=10;

        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";
        final int expectedItemsCount=0;


        final SearchQuery aQuery = new SearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);


        final Pagination<Genre> expectedPagination = new Pagination<Genre>(expectedPage,expectedPerPage,genres.size(),genres);



        final Pagination<GenreListOutput> expectedResult = expectedPagination.map(GenreListOutput::from);

        Mockito.when(genreGateway.findAll(Mockito.eq(aQuery)))
                .thenReturn(expectedPagination);

        final Pagination<GenreListOutput> actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount,actualResult.items().size());
        Assertions.assertEquals(expectedResult,actualResult);
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(genres.size(),actualResult.total());
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


        Mockito.when(genreGateway.findAll(Mockito.eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, ()->
                useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());
    }

}
