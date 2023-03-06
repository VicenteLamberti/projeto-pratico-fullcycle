package com.fullcycle.vicente.application.genre.delete;


import com.fullcycle.vicente.application.UseCaseTest;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreGateway;
import com.fullcycle.vicente.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }
    @InjectMocks
    DefaultDeleteGenreUseCase useCase;

    @Mock
    GenreGateway genreGateway;

    @Test
    public void givenAValidId_whenCallsDeleteGenre_shouldBeOk(){
        final Genre aGenre = Genre.newGenre("Terror",  true);
        final GenreID aExpectedId = aGenre.getId();

        Mockito.doNothing()
                .when(genreGateway).deleteById(aExpectedId);
        Assertions.assertDoesNotThrow(()->useCase.execute(aExpectedId.getValue()));

        Mockito.verify(genreGateway,Mockito.times(1)).deleteById(aExpectedId);
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteGenre_shouldBeOk(){
        final GenreID expectedId = GenreID.from("123");

        Mockito.doNothing()
                .when(genreGateway).deleteById(expectedId);
        Assertions.assertDoesNotThrow(()->useCase.execute(expectedId.getValue()));

        Mockito.verify(genreGateway,Mockito.times(1)).deleteById(expectedId);

    }

    @Test
    public void givenAValid_whenGatewayThrowsException_shouldReturnException(){
        final Genre aGenre = Genre.newGenre("Filmes", true);
        final GenreID expectedId = aGenre.getId();


        Mockito.doThrow(new IllegalStateException("Gatewat error"))
                .when(genreGateway).deleteById(expectedId);
        Assertions.assertThrows(IllegalStateException.class, ()->useCase.execute(expectedId.getValue()));

        Mockito.verify(genreGateway,Mockito.times(1)).deleteById(expectedId);
    }



}
