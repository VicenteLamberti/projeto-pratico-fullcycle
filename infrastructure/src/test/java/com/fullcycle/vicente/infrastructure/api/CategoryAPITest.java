package com.fullcycle.vicente.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.vicente.ControllerTest;
import com.fullcycle.vicente.application.category.create.CreateCategoryOutput;
import com.fullcycle.vicente.application.category.create.CreateCategoryUseCase;
import com.fullcycle.vicente.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.vicente.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.vicente.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.DomainException;
import com.fullcycle.vicente.domain.validation.Error;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import com.fullcycle.vicente.infrastructure.category.models.CreateCategoryApiInput;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;
import java.util.Optional;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final CreateCategoryApiInput aInput = new CreateCategoryApiInput(expectedName,expectedDescription,expectedIsActive);

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

        final CreateCategoryApiInput aInput = new CreateCategoryApiInput(expectedName,expectedDescription,expectedIsActive);

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

        final CreateCategoryApiInput aInput = new CreateCategoryApiInput(expectedName,expectedDescription,expectedIsActive);

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
        final String aExpectedId = CategoryID.from("123").getValue();



        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(
                        new Error("Category with ID 123 was not found".formatted(aExpectedId))
                ));


        final var request = MockMvcRequestBuilders
                .get("/categories/{id}", aExpectedId)
                .contentType(MediaType.APPLICATION_JSON);




        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));


    }

}
