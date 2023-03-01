package com.fullcycle.vicente.application.genre.update;

import com.fullcycle.vicente.application.UseCaseTest;
import com.fullcycle.vicente.application.category.update.UpdateCategoryCommand;
import com.fullcycle.vicente.application.genre.create.CreateGenreOutput;
import com.fullcycle.vicente.application.genre.create.DefaultCreateGenreUseCase;
import com.fullcycle.vicente.application.genre.update.DefaultUpdateGenreUseCase;
import com.fullcycle.vicente.application.genre.update.UpdateGenreCommand;
import com.fullcycle.vicente.application.genre.update.UpdateGenreOutput;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.DomainException;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreGateway;
import com.fullcycle.vicente.domain.genre.GenreID;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UpdateGenreUseCaseTest extends UseCaseTest {
    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway,categoryGateway);
    }

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() throws InterruptedException {
        //given

        final Genre aGenre = Genre.newGenre("acao", true);

        final GenreID expectedId = aGenre.getId();
        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.<CategoryID>of();

        final UpdateGenreCommand aCommand = UpdateGenreCommand.with(
          expectedId.getValue(),
          expectedName,
          expectedIsActive,
          asString(expectedCategories)

        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        Thread.sleep(3);
        // when
        final UpdateGenreOutput actualOutput = useCase.execute(aCommand);

        //then

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(updatedGenre->
                        Objects.equals(expectedId, updatedGenre.getId())
                        && Objects.equals(expectedName, updatedGenre.getName())
                        && Objects.equals(expectedIsActive, updatedGenre.isActive())
                        && Objects.equals(expectedCategories, updatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
                        && Objects.isNull(updatedGenre.getDeletedAt())


        ));
    }


    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() throws InterruptedException {
        //given

        final Genre aGenre = Genre.newGenre("acao", true);

        final GenreID expectedId = aGenre.getId();
        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.of(CategoryID.from("123"), CategoryID.from("456"));

        final UpdateGenreCommand aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)

        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        Mockito.when(categoryGateway.existsByIds(expectedCategories)).thenReturn(expectedCategories);

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        Thread.sleep(3);
        // when
        final UpdateGenreOutput actualOutput = useCase.execute(aCommand);

        //then

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(updatedGenre->
                Objects.equals(expectedId, updatedGenre.getId())
                        && Objects.equals(expectedName, updatedGenre.getName())
                        && Objects.equals(expectedIsActive, updatedGenre.isActive())
                        && Objects.equals(expectedCategories, updatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
                        && Objects.isNull(updatedGenre.getDeletedAt())


        ));
    }


    @Test
    public void givenAInvalidName_whenCallUpdateGenre_thenShouldReturnDomainException(){
        final Genre aGenre = Genre.newGenre("acao", true);

        final GenreID expectedId = aGenre.getId();
        final String expectedName = null;
        final Boolean expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.<CategoryID>of();
        final String expectedErrorMessage = "'name' should not be null";
        final int expectedErrorCount = 1;




        final UpdateGenreCommand aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories));

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of((Genre.clone(aGenre))));

        DomainException actualException = Assertions.assertThrows(DomainException.class, ()->{
           useCase.execute(aCommand);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());

    }

    @Test
    public void givenAInvalidNameAndSomeCategoriesNotFound_whenCallUpdateGenre_thenShouldReturnDomainException(){
        final Genre aGenre = Genre.newGenre("acao", true);
        final CategoryID filmes = CategoryID.from("123");
        final CategoryID series = CategoryID.from("456");


        final GenreID expectedId = aGenre.getId();
        final String expectedName = null;
        final Boolean expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.<CategoryID>of(filmes,series);
        final String expectedErrorMessage1 = "Some categories could not be found: 456";
        final String expectedErrorMessage2 = "'name' should not be null";

        final int expectedErrorCount = 2;




        final UpdateGenreCommand aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories));

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of((Genre.clone(aGenre))));

        Mockito.when(categoryGateway.existsByIds(expectedCategories)).thenReturn(List.of(filmes));
        DomainException actualException = Assertions.assertThrows(DomainException.class, ()->{
            useCase.execute(aCommand);
        });

        Assertions.assertEquals(expectedErrorMessage1, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());

    }


    private List<String> asString(final List<CategoryID> categories ){
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
