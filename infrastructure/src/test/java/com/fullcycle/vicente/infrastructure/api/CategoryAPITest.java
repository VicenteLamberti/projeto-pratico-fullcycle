package com.fullcycle.vicente.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.vicente.ControllerTest;
import com.fullcycle.vicente.application.category.create.CreateCategoryOutput;
import com.fullcycle.vicente.application.category.create.CreateCategoryUseCase;
import com.fullcycle.vicente.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.vicente.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.vicente.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.vicente.application.category.retrieve.list.CategoryListOutput;
import com.fullcycle.vicente.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.vicente.application.category.update.UpdateCategoryOutput;
import com.fullcycle.vicente.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.DomainException;
import com.fullcycle.vicente.domain.exceptions.NotFoundException;
import com.fullcycle.vicente.domain.pagination.Pagination;
import com.fullcycle.vicente.domain.validation.Error;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import com.fullcycle.vicente.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.vicente.infrastructure.category.models.UpdateCategoryRequest;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoryUseCase;


    @Autowired
    private ObjectMapper mapper;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final CreateCategoryRequest aInput = new CreateCategoryRequest(expectedName,expectedDescription,expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(CreateCategoryOutput.from("123")));

        final var request = MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().string("Location","/categories/123"),
                        MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123"))
                );

        Mockito.verify(createCategoryUseCase,Mockito.times(1)).execute(Mockito.argThat(
                cmd -> Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())

        ));

    }



    @Test
    public void givenAInvalidName_whenCallCreateCategory_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedMessage = "'name' should not be null";

        final CreateCategoryRequest aInput = new CreateCategoryRequest(expectedName,expectedDescription,expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedMessage))));

        final var request = MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isUnprocessableEntity(),
                        MockMvcResultMatchers.header().string("Location",Matchers.nullValue()),
                        MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage))
                );

        Mockito.verify(createCategoryUseCase,Mockito.times(1)).execute(Mockito.argThat(
                cmd -> Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())

        ));

    }


    @Test
    public void givenAInvalidCommand_whenCallCreateCategory_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedMessage = "'name' should not be null";

        final CreateCategoryRequest aInput = new CreateCategoryRequest(expectedName,expectedDescription,expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        final var request = MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isUnprocessableEntity(),
                        MockMvcResultMatchers.header().string("Location",Matchers.nullValue()),
                        MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage))
                );

        Mockito.verify(createCategoryUseCase,Mockito.times(1)).execute(Mockito.argThat(
                cmd -> Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())

        ));

    }


    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception{
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final Category aCategory = Category.newCategory(expectedName,expectedDescription, expectedIsActive);
        final String aExpectedId = aCategory.getId().getValue();


        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenReturn(CategoryOutput.from(aCategory));


        final var request = MockMvcRequestBuilders
                .get("/categories/{id}", aExpectedId)
                .contentType(MediaType.APPLICATION_JSON);


        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());


        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(aExpectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)));

        Mockito.verify(getCategoryByIdUseCase,Mockito.times(1)).execute(aExpectedId);
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        final String expectedErrorMessage = "Category with ID 123 was not found";
        final CategoryID aExpectedId = CategoryID.from("123");



        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, aExpectedId));


        final var request = MockMvcRequestBuilders
                .get("/categories/{id}", aExpectedId)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));


    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception{
        final String expectedId = "123";
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(UpdateCategoryOutput.from(expectedId)));

        final UpdateCategoryRequest aInput = new UpdateCategoryRequest(expectedName,expectedDescription,expectedIsActive);
        final var request = MockMvcRequestBuilders
                .put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));


        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());


        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(updateCategoryUseCase,Mockito.times(1)).execute(Mockito.argThat(comandoInput ->
                Objects.equals(expectedName,comandoInput.name())
                        &&  Objects.equals(expectedDescription, comandoInput.description())
                        &&  Objects.equals(expectedIsActive, comandoInput.isActive())
        ));
    }


    @Test
    public void givenAInvalidId_whenGatewayThrowsRandomException_shouldReturnAException() throws Exception{
        final String expectedId = "not-found";
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedErrorMessage = "Category with ID not-found was not found";


        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class,CategoryID.from(expectedId)));

        final UpdateCategoryRequest aInput = new UpdateCategoryRequest(expectedName,expectedDescription,expectedIsActive);
        final var request = MockMvcRequestBuilders
                .put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));


        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());


        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateCategoryUseCase,Mockito.times(1)).execute(Mockito.argThat(comandoInput ->
                Objects.equals(expectedName,comandoInput.name())
                        &&  Objects.equals(expectedDescription, comandoInput.description())
                        &&  Objects.equals(expectedIsActive, comandoInput.isActive())
        ));



    }


    @Test
    public void givenAInvalidName_whenCallUpdateCategory_thenShouldReturnDomainException() throws Exception{
        final String expectedId = "123";
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedMessage = "'name' should not be null";


        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedMessage))));

        final UpdateCategoryRequest aInput = new UpdateCategoryRequest(expectedName,expectedDescription,expectedIsActive);
        final var request = MockMvcRequestBuilders
                .put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));


        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());


        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage)));

        Mockito.verify(updateCategoryUseCase,Mockito.times(1)).execute(Mockito.argThat(comandoInput ->
                Objects.equals(expectedName,comandoInput.name())
                        &&  Objects.equals(expectedDescription, comandoInput.description())
                        &&  Objects.equals(expectedIsActive, comandoInput.isActive())
        ));



    }



    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldReturnNoContent() throws Exception{
        final String expectedId = "123";
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;




        Mockito.doNothing()
                .when(deleteCategoryUseCase).execute(Mockito.any());


        final var request = MockMvcRequestBuilders
                .delete("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON);


        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());


        response.andExpect(MockMvcResultMatchers.status().isNoContent());


        Mockito.verify(deleteCategoryUseCase,Mockito.times(1)).execute(expectedId);
    }


    @Test
    public void givenAValidParam_whenCallsListCategories_shouldReturnCategories() throws Exception {
        final Category aCategory = Category.newCategory("Movies", null, true);

        final List<CategoryListOutput> expectedItems = List.of(CategoryListOutput.from(aCategory));


        final int expectedPage = 0;

        final int expectedPerPage=10;

        final String expectedTerms = "movies";
        final String expectedSort = "description";
        final String expectedDirection = "desc";
        final int expectedItemsCount = 1;
        final int expectedTotal = 1;

        Mockito.when(listCategoryUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage,expectedPerPage,expectedTotal,expectedItems));


        final var request = MockMvcRequestBuilders
                .get("/categories")
                .queryParam("page",String.valueOf(expectedPage))
                .queryParam("perPage",String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);



        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());


        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aCategory.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aCategory.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(aCategory.getDescription())));

        Mockito.verify(listCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(query->
                     Objects.equals(expectedPage, query.page())
                && Objects.equals(expectedPerPage,query.perPage())
                && Objects.equals(expectedDirection, query.direction())
                && Objects.equals(expectedSort, query.sort())
                && Objects.equals(expectedTerms, query.terms())
                ));


    }
}
