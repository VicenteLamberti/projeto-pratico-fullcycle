package com.fullcycle.vicente.e2e.category;

import com.fullcycle.vicente.E2ETest;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.infrastructure.category.models.CategoryResponse;
import com.fullcycle.vicente.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.vicente.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.vicente.infrastructure.configuration.json.Json;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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

    @Test
    public void AsACatalogAdminIsShouldBeAbleToNavigateToAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0,categoryRepository.count());

        givenACategory("Filmes",null,true);
        givenACategory("Documentarios",null,true);
        givenACategory("Séries",null,true);

        listCategories(0,1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Documentarios")));

        listCategories(1,1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Filmes")));

        listCategories(2,1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Séries")));

        listCategories(3,1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));
    }

    @Test
    public void AsACatalogAdminIsShouldBeAbleToSearchBetweenToAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0,categoryRepository.count());

        givenACategory("Filmes",null,true);
        givenACategory("Documentarios",null,true);
        givenACategory("Séries",null,true);

        listCategories(0,1,"fil")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Filmes")));


    }

    @Test
    public void AsACatalogAdminIsShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0,categoryRepository.count());

        givenACategory("Filmes","C",true);
        givenACategory("Documentarios","Z",true);
        givenACategory("Séries","A",true);

        listCategories(0,3,"","description","desc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Documentarios")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", Matchers.equalTo("Filmes")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", Matchers.equalTo("Séries")));


    }


    @Test
    public void asACatalogAdminIsShouldBeAbleToGetACategoryByItsIdentifier() throws Exception {

        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        Assertions.assertEquals(0,categoryRepository.count());
        final CategoryID actualId = givenACategory(expectedName,expectedDescription,expectedIsActive);
        final CategoryJPAEntity actualCategory = categoryRepository.findById(actualId.getValue()).get();


        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }


    @Test
    public void asACatalogAdminIsShouldBeAbleToSeeATretatedErrorByGettingNotFound() throws Exception {
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders
                .get("/categories/123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.equalTo("Category with ID 123 was not found")));

    }


    @Test
    public void asACatalogAdminIsShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
        Assertions.assertEquals(0,categoryRepository.count());
        final CategoryID actualId = givenACategory("Movies",null,true);

        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final UpdateCategoryRequest aRequestBody =  new UpdateCategoryRequest(expectedName,expectedDescription,expectedIsActive);

        final MockHttpServletRequestBuilder aRequest =
                MockMvcRequestBuilders
                        .put("/categories/"+actualId.getValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aRequestBody));



        this.mvc.perform(aRequest)
                        .andExpect(MockMvcResultMatchers.status().isOk());

        final CategoryJPAEntity actualCategory = categoryRepository.findById(actualId.getValue()).get();


        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIsShouldBeAbleToInactivateACategoryByItsIdentifier() throws Exception {

        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;


        Assertions.assertEquals(0,categoryRepository.count());
        final CategoryID actualId = givenACategory(expectedName,expectedDescription,true);



        final UpdateCategoryRequest aRequestBody =  new UpdateCategoryRequest(expectedName,expectedDescription,expectedIsActive);

        final MockHttpServletRequestBuilder aRequest =
                MockMvcRequestBuilders
                        .put("/categories/"+actualId.getValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aRequestBody));



        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk());

        final CategoryJPAEntity actualCategory = categoryRepository.findById(actualId.getValue()).get();


        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIsShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {

        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;


        Assertions.assertEquals(0,categoryRepository.count());
        final CategoryID actualId = givenACategory(expectedName,expectedDescription,false);



        final UpdateCategoryRequest aRequestBody =  new UpdateCategoryRequest(expectedName,expectedDescription,expectedIsActive);

        final MockHttpServletRequestBuilder aRequest =
                MockMvcRequestBuilders
                        .put("/categories/"+actualId.getValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aRequestBody));



        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk());

        final CategoryJPAEntity actualCategory = categoryRepository.findById(actualId.getValue()).get();


        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }



    private ResultActions listCategories(final int page, final int perPage, String search) throws Exception {
        return listCategories(page,perPage,search,"","");
    }
    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page,perPage,"","","");
    }
    private ResultActions listCategories(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {

        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders
                .get("/categories")
                .queryParam("page",String.valueOf(page))
                .queryParam("perPage",String.valueOf(perPage))
                .queryParam("search",search)
                .queryParam("sort",sort)
                .queryParam("dir",direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc.perform(aRequest);
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
