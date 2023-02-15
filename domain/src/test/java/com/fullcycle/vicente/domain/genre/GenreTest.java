package com.fullcycle.vicente.domain.genre;


import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.DomainException;
import com.fullcycle.vicente.domain.exceptions.NotificationException;
import com.fullcycle.vicente.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void givenAActiveGenre_whenCallDeactivate_shouldReceiveOk() throws InterruptedException {

        final String expectedName="Ação";
        final boolean expectedIsActive = false;
        final int expectedCategories = 0;


        final Genre actualGenre = Genre.newGenre(expectedName,true);
        Instant actualCreatedAt = actualGenre.getCreatedAt();
        Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(2);// para o teste funcionar

        Assertions.assertNotNull(actualGenre);
        actualGenre.deactivate();


        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(actualGenre.getName());
        Assertions.assertNotNull(actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories().size());
        Assertions.assertEquals(actualCreatedAt,actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }


    @Test
    public void givenAInactiveGenre_whenCallActivate_shouldReceiveOk() throws InterruptedException {

        final String expectedName="Ação";
        final boolean expectedIsActive = true;
        final int expectedCategories = 0;


        final Genre actualGenre = Genre.newGenre(expectedName,false);
        Instant actualCreatedAt = actualGenre.getCreatedAt();
        Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(2);// para o teste funcionar

        Assertions.assertNotNull(actualGenre);
        actualGenre.activate();


        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(actualGenre.getName());
        Assertions.assertNotNull(actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories().size());
        Assertions.assertEquals(actualCreatedAt,actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidInactivateGenre_whenCallUpdate_shouldReceiveOGenreUpdated() throws InterruptedException {

        final String expectedName="Ação";
        final boolean expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.of(CategoryID.from("123"));


        final Genre actualGenre = Genre.newGenre("acao",false);

        Instant actualCreatedAt = actualGenre.getCreatedAt();
        Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(2);// para o teste funcionar

        Assertions.assertNotNull(actualGenre);
        actualGenre.update(expectedName,expectedIsActive,expectedCategories);


        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(actualGenre.getName());
        Assertions.assertNotNull(actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt,actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }



    @Test
    public void givenAValidActivateGenre_whenCallUpdate_shouldReceiveOGenreUpdated() throws InterruptedException {

        final String expectedName="Ação";
        final boolean expectedIsActive = false;
        final List<CategoryID> expectedCategories = List.of(CategoryID.from("123"));


        final Genre actualGenre = Genre.newGenre("acao",true);

        Instant actualCreatedAt = actualGenre.getCreatedAt();
        Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(2);// para o teste funcionar

        Assertions.assertNotNull(actualGenre);
        actualGenre.update(expectedName,expectedIsActive,expectedCategories);


        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(actualGenre.getName());
        Assertions.assertNotNull(actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt,actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithInvalidProperty_shouldReceiveNotificationException() throws InterruptedException {

        final String expectedName=" ";
        final boolean expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.of(CategoryID.from("123"));


        final Genre actualGenre = Genre.newGenre("acao",false);

        Thread.sleep(2);// para o teste funcionar



        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be empty";


        final NotificationException actualException = Assertions.assertThrows(NotificationException.class, ()->{
            actualGenre.update(expectedName,expectedIsActive,expectedCategories);
        });

        Assertions.assertEquals(expectedErrorCount,actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,actualException.getErrors().get(0).message());



    }


    @Test
    public void givenAValidGenre_whenCallUpdateWithInvalidPropertyCategoriesNull_shouldReceiveOK_becauseCleanList() throws InterruptedException {

        final String expectedName="Ação";
        final boolean expectedIsActive = true;
        final List<CategoryID> expectedCategories = new ArrayList<CategoryID>();

        final Genre actualGenre = Genre.newGenre("acao",false);

        Instant actualCreatedAt =actualGenre.getCreatedAt();
        Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(2);// para o teste funcionar

        Assertions.assertDoesNotThrow(()->{
            actualGenre.update(expectedName,expectedIsActive,null);
        });

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(actualGenre.getName());
        Assertions.assertNotNull(actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt,actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

    }


    @Test
    public void givenAValidGenreWithoutCategories_whenCallAddCategory_shouldReceiveOK() throws InterruptedException {

        final String expectedName="Ação";
        final boolean expectedIsActive = true;

        final CategoryID seriesID = CategoryID.from("123");
        final CategoryID moviesID = CategoryID.from("456");
        final List<CategoryID> expectedCategories = List.of(seriesID,moviesID);



        final Genre actualGenre = Genre.newGenre("acao",false);

        Instant actualCreatedAt =actualGenre.getCreatedAt();
        Instant actualUpdatedAt = actualGenre.getUpdatedAt();
        Assertions.assertEquals(0,actualGenre.getCategories().size());

        Thread.sleep(2);// para o teste funcionar

        actualGenre.addCategory(seriesID);
        actualGenre.addCategory(moviesID);


        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(actualGenre.getName());
        Assertions.assertNotNull(actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt,actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOK() throws InterruptedException {

        final String expectedName="Ação";
        final boolean expectedIsActive = true;

        final CategoryID seriesID = CategoryID.from("123");
        final CategoryID moviesID = CategoryID.from("456");

        final List<CategoryID> expectedCategories = List.of(seriesID);



        final Genre actualGenre = Genre.newGenre("acao",false);
        actualGenre.update(expectedName,expectedIsActive,List.of(seriesID,moviesID));

        Instant actualCreatedAt =actualGenre.getCreatedAt();
        Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        Assertions.assertEquals(2,actualGenre.getCategories().size());

        Thread.sleep(2);// para o teste funcionar

        actualGenre.removeCategory(moviesID);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(actualGenre.getName());
        Assertions.assertNotNull(actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt,actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

    }


    @Test
    public void givenAValidGenreWithoutCategories_whenCallAddCategoryNull_shouldReceiveOK() throws InterruptedException {

        final String expectedName="Ação";
        final boolean expectedIsActive = true;

        final CategoryID seriesID = CategoryID.from("123");
        final CategoryID moviesID = CategoryID.from("456");
        final List<CategoryID> expectedCategories = new ArrayList<CategoryID>();



        final Genre actualGenre = Genre.newGenre("acao",false);

        Instant actualCreatedAt =actualGenre.getCreatedAt();
        Instant actualUpdatedAt = actualGenre.getUpdatedAt();
        Assertions.assertEquals(0,actualGenre.getCategories().size());

        Thread.sleep(3);// para o teste funcionar

        actualGenre.addCategory(null);



        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(actualGenre.getName());
        Assertions.assertNotNull(actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt,actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt,actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

    }


    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategoryNull_shouldReceiveOK() throws InterruptedException {

        final String expectedName="Ação";
        final boolean expectedIsActive = true;

        final CategoryID seriesID = CategoryID.from("123");
        final CategoryID moviesID = CategoryID.from("456");

        final List<CategoryID> expectedCategories = List.of(seriesID,moviesID);



        final Genre actualGenre = Genre.newGenre("acao",false);
        actualGenre.update(expectedName,expectedIsActive,expectedCategories);

        Instant actualCreatedAt =actualGenre.getCreatedAt();
        Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        Assertions.assertEquals(2,actualGenre.getCategories().size());

        Thread.sleep(2);// para o teste funcionar

        actualGenre.removeCategory(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertNotNull(actualGenre.getName());
        Assertions.assertNotNull(actualGenre.isActive());
        Assertions.assertEquals(expectedCategories,actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt,actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt,actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

    }
}
