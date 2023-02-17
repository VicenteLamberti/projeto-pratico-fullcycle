package genre.create;

import com.fullcycle.vicente.application.genre.create.CreateGenreCommand;
import com.fullcycle.vicente.application.genre.create.CreateGenreOutput;
import com.fullcycle.vicente.application.genre.create.DefaultCreateGenreUseCase;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.DomainException;
import com.fullcycle.vicente.domain.exceptions.NotificationException;
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
        final CreateGenreOutput actualOutput = useCase.execute(aCommand);


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


    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId(){
        //given

        final String expectedName = "Ação";
        final boolean expectedIsActive =false;
        final List<CategoryID> expectedCategories = List.<CategoryID>of();

        final CreateGenreCommand aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));

        Mockito.when(genreGateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final CreateGenreOutput actualOutput = useCase.execute(aCommand);


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
                        && Objects.nonNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    public void giverAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId(){

        //given

        final String expectedName = "Ação";
        final boolean expectedIsActive =true;
        final List<CategoryID> expectedCategories = List.<CategoryID>of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final CreateGenreCommand aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(expectedCategories);

        Mockito.when(genreGateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());



        //when
        final CreateGenreOutput actualOutput = useCase.execute(aCommand);


        //then

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway,Mockito.times(1)).existsByIds(expectedCategories);

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

    @Test
    public void givenAnvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException(){

        //given

        final String expectedName = " ";
        final boolean expectedIsActive =true;
        final List<CategoryID> expectedCategories = List.<CategoryID>of();

        final String expectedErrorMessage = "'name' should not be empty";
        final int expectedErrorCount=1;

        final CreateGenreCommand aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));



        //when
        final NotificationException actualException =
                Assertions.assertThrows(NotificationException.class, ()->{
                   useCase.execute(aCommand);
                });


        //then

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway,Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway,Mockito.times(0)).create(Mockito.any());

    }


    @Test
    public void givenAnvalidNullName_whenCallsCreateGenre_shouldReturnDomainException(){

        //given

        final String expectedName = null;
        final boolean expectedIsActive =true;
        final List<CategoryID> expectedCategories = List.<CategoryID>of();

        final String expectedErrorMessage = "'name' should not be null";
        final int expectedErrorCount=1;

        final CreateGenreCommand aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));



        //when
        final NotificationException actualException =
                Assertions.assertThrows(NotificationException.class, ()->{
                    useCase.execute(aCommand);
                });


        //then

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway,Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway,Mockito.times(0)).create(Mockito.any());

    }

    @Test
    public void givenAValidACommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException(){

        //given

        final CategoryID series = CategoryID.from("123");
        final CategoryID filmes = CategoryID.from("456");
        final CategoryID documentarios = CategoryID.from("789");

        final String expectedName = "Ação";
        final boolean expectedIsActive =true;
        final List<CategoryID> expectedCategories = List.<CategoryID>of(
                series,
                filmes,
                documentarios
        );

        final String expectedErrorMessage = "Some categories could not be found: 456, 789";
        final int expectedErrorCount=1;

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(series));

        final CreateGenreCommand aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));



        //when
        final NotificationException actualException =
                Assertions.assertThrows(NotificationException.class, ()->{
                    useCase.execute(aCommand);
                });


        //then

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway,Mockito.times(1)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway,Mockito.times(0)).create(Mockito.any());

    }


    @Test
    public void givenInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException(){

        //given

        final CategoryID series = CategoryID.from("123");
        final CategoryID filmes = CategoryID.from("456");
        final CategoryID documentarios = CategoryID.from("789");

        final String expectedName = " ";
        final boolean expectedIsActive =true;
        final List<CategoryID> expectedCategories = List.<CategoryID>of(
                series,
                filmes,
                documentarios
        );

        final String expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final String expectedErrorMessageTwo = "'name' should not be empty";
        final int expectedErrorCount=2;

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(series));

        final CreateGenreCommand aCommand = CreateGenreCommand.with(expectedName,expectedIsActive, asString(expectedCategories));



        //when
        final NotificationException actualException =
                Assertions.assertThrows(NotificationException.class, ()->{
                    useCase.execute(aCommand);
                });


        //then

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        Mockito.verify(categoryGateway,Mockito.times(1)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway,Mockito.times(0)).create(Mockito.any());

    }

    private List<String> asString(final List<CategoryID> categories ){
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
