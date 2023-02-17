package genre.update;

import com.fullcycle.vicente.application.category.update.UpdateCategoryCommand;
import com.fullcycle.vicente.application.genre.create.CreateGenreOutput;
import com.fullcycle.vicente.application.genre.create.DefaultCreateGenreUseCase;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreGateway;
import com.fullcycle.vicente.domain.genre.GenreID;
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

@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseTest {

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
          expectedCategories

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
}
