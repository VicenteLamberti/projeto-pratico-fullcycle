package com.fullcycle.vicente.application.genre.retrieve.get;


import com.fullcycle.vicente.application.UseCaseTest;
import com.fullcycle.vicente.application.genre.delete.DefaultDeleteGenreUseCase;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreGateway;
import com.fullcycle.vicente.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    DefaultGetGenreByIdUseCase useCase;

    @Mock
    GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre(){

        final String expectedName = "Drama";
        final Boolean expectedActive = true;
        final List<CategoryID> expectedCategories = List.of(CategoryID.from("123"),CategoryID.from("456"));

        Genre aGenre = Genre.newGenre(expectedName,expectedActive);
        aGenre.addCategories(expectedCategories);

        GenreID expectedId = aGenre.getId();

        Mockito.when(genreGateway.findById(expectedId))
                .thenReturn(Optional.of(Genre.clone(aGenre)));

        GenreOutput output = useCase.execute(expectedId.getValue());


        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedActive, output.isActive());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
        Assertions.assertNull(output.deletedAt());
        Assertions.assertEquals(expectedCategories,output.categories());



    }

}
