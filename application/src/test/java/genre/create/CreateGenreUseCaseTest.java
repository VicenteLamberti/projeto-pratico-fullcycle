package genre.create;

import com.fullcycle.vicente.application.genre.create.CreateGenreCommand;
import com.fullcycle.vicente.application.genre.create.DefaultCreateGenreUseCase;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.genre.GenreGateway;
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

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId(){
        //given

        final String expectedName = "Ação";
        final boolean expectedIsActive =true;
        final List<CategoryID> expectedCategories = List.<CategoryID>of();

        final CreateGenreCommand aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));

        Mockito.when(genreGateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(aCommand);


        //then

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Mockito.verify(genreGateway,Mockito.times(1)).create(Mockito.argThat(aGenre->
                        Objects.equals(expectedName, aGenre.getName())
                && Objects.equals(expectedIsActive, aGenre.isActive())
                && Objects.equals(expectedCategories, aGenre.getCategories())
                && Objects.nonNull(aGenre.getId())
                && Objects.nonNull(aGenre.getCreatedAt())
                && Objects.nonNull(aGenre.getUpdatedAt())
                && Objects.isNull(aGenre.getDeletedAt())
                ));
    }

    private List<String> asString(final List<CategoryID> categories ){
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
