package com.fullcycle.vicente.e2e.category;

import com.fullcycle.vicente.ControllerTest;
import com.fullcycle.vicente.E2ETest;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.infrastructure.category.models.CategoryResponse;
import com.fullcycle.vicente.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.vicente.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Container
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    .withPassword("123456")
                    .withUsername("root")
                    .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry){
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
        registry.add("mysql.port", () -> mappedPort);



    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void testWorks(){
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    }

    @Test
    public void asACatalogAdminIsShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {

        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        Assertions.assertEquals(0,categoryRepository.count());
        final CategoryID actualId = givenACategory(expectedName,expectedDescription,expectedIsActive);
        final CategoryResponse actualCategory = retrieveACategory(actualId.getValue());


        Assertions.assertEquals(expectedName,actualCategory.name());
        Assertions.assertEquals(expectedDescription,actualCategory.description());
        Assertions.assertEquals(expectedIsActive,actualCategory.active());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNotNull(actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());
    }

    private CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        CreateCategoryRequest aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final String actualId = this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/categories/","");
        return CategoryID.from(actualId);
    }

    private CategoryResponse retrieveACategory(final String anId) throws Exception {
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders
                .get("/categories/" + anId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final String json = this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Json.readValue(json,CategoryResponse.class);
    }

}
