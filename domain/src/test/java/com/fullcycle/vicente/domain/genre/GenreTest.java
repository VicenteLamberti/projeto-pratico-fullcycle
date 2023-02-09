package com.fullcycle.vicente.domain.genre;


import com.fullcycle.vicente.domain.exceptions.DomainException;
import com.fullcycle.vicente.domain.exceptions.NotificationException;
import com.fullcycle.vicente.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_shouldInstantiateGenre(){
        final String expectedName="Ação";
        final boolean expectedIsActive = true;
        final int expectedCategories = 0;


        final Genre actualGenre = Genre.newGenre(expectedName,expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(actualGenre.getName());
        Assertions.assertNotNull(actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

    }
    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError(){
        final String expectedName=null;
        final boolean expectedIsActive = true;

        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be null";


        final NotificationException actualException = Assertions.assertThrows(NotificationException.class, ()->{
           Genre.newGenre(expectedName,expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount,actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,actualException.getErrors().get(0).message());


    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAError(){
        final String expectedName=" ";
        final boolean expectedIsActive = true;

        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be empty";


        final NotificationException actualException = Assertions.assertThrows(NotificationException.class, ()->{
            Genre.newGenre(expectedName,expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount,actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,actualException.getErrors().get(0).message());


    }

    @Test
    public void givenInvalidNameLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAError(){
        final String expectedName = """
                          Não obstante, a execução dos pontos do programa promove a alavancagem do 
                          levantamento das variáveis envolvidas. Podemos já vislumbrar o modo pelo qual a 
                          expansão dos mercados mundiais apresenta tendências no sentido de aprovar a manutenção dos níveis de 
                          motivação departamental. A certificação de metodologias que nos auxiliam a lidar com o desenvolvimento 
                          contínuo de distintas formas de atuação acarreta um processo de reformulação e modernização das condições 
                          inegavelmente apropriadas. No mundo atual, a estrutura atual da organização estende o alcance e a 
                          importância dos modos de operação convencionais. A prática 
                          cotidiana prova que o novo modelo estrutural aqui preconizado representa uma abertura 
                          para a melhoria do orçamento setorial.
                """;
        final boolean expectedIsActive = true;

        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should  be  than 1 character and 255 character";


        final NotificationException actualException = Assertions.assertThrows(NotificationException.class, ()->{
            Genre.newGenre(expectedName,expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount,actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,actualException.getErrors().get(0).message());


    }
}
