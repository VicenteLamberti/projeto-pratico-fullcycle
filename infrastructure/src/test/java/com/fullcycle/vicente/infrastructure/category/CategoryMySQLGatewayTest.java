package com.fullcycle.vicente.infrastructure.category;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.category.CategorySearchQuery;
import com.fullcycle.vicente.domain.pagination.Pagination;
import com.fullcycle.vicente.infrastructure.MySQLGatewayTest;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Foi necessário adicionar esse ComponentScan, pois Utilizando o DataJpaTest, ele não consegue enxergar o service CategoryMySQLGateway
 * Se fosse o SprintBootTest, funcionaria
 */
@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnsANewCategory(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedActive = true;

        final int expectedQuantityRegisterInBankBeforePersist = 0;

        Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);
        Assertions.assertEquals(expectedQuantityRegisterInBankBeforePersist, categoryRepository.count());

        Category actualCategory = categoryMySQLGateway.create(aCategory);

        Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
        Assertions.assertEquals(aCategory.getName(),actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(),actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(),actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.getDeletedAt());

        CategoryJPAEntity actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(),actualEntity.getId());
        Assertions.assertEquals(expectedName,actualEntity.getName());
        Assertions.assertEquals(expectedDescription,actualEntity.getDescription());
        Assertions.assertEquals(expectedActive,actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),actualEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(),actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());

    }



    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedActive = true;

        final int expectedQuantityRegisterInBankBeforePersist = 0;
        final int expectedQuantityRegisterInBankAfterPersist = 1;

        Assertions.assertEquals(expectedQuantityRegisterInBankBeforePersist, categoryRepository.count());

        Category aCategory = Category.newCategory("Name Invalid", null, expectedActive);

        categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));

        Assertions.assertEquals(expectedQuantityRegisterInBankAfterPersist, categoryRepository.count());

        final Category aUpdatedCategory = aCategory.clone().update(expectedName,expectedDescription,expectedActive);

        Category actualCategory = categoryMySQLGateway.update(aUpdatedCategory);

        Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedActive,actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.getDeletedAt());

        CategoryJPAEntity actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(),actualEntity.getId());
        Assertions.assertEquals(expectedName,actualEntity.getName());
        Assertions.assertEquals(expectedDescription,actualEntity.getDescription());
        Assertions.assertEquals(expectedActive,actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(),actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());

    }


    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory(){
        Category aCategory = Category.newCategory("Filmes", null, true);

        final int expectedQuantityRegisterInBankBeforePersist = 0;
        final int expectedQuantityRegisterInBankAfterPersist = 1;

        Assertions.assertEquals(expectedQuantityRegisterInBankBeforePersist, categoryRepository.count());


        categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));

        Assertions.assertEquals(expectedQuantityRegisterInBankAfterPersist, categoryRepository.count());
        categoryMySQLGateway.deleteById(aCategory.getId());

    }

    @Test
    public void givenAInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory(){
        final int expectedQuantityRegisterInBankBeforePersist = 0;
        Assertions.assertEquals(expectedQuantityRegisterInBankBeforePersist, categoryRepository.count());
        categoryMySQLGateway.deleteById(CategoryID.from("invalid"));
        Assertions.assertEquals(expectedQuantityRegisterInBankBeforePersist, categoryRepository.count());

    }


    @Test
    public void givenAPrepErsistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedActive = true;

        final int expectedQuantityRegisterInBankBeforePersist = 0;
        final int expectedQuantityRegisterInBankAfterPersist = 1;

        Assertions.assertEquals(expectedQuantityRegisterInBankBeforePersist, categoryRepository.count());

        Category aCategory = Category.newCategory(expectedName ,expectedDescription, expectedActive);

        categoryRepository.saveAndFlush(CategoryJPAEntity.from(aCategory));

        Assertions.assertEquals(expectedQuantityRegisterInBankAfterPersist, categoryRepository.count());


        Category actualCategory = categoryMySQLGateway.findById(aCategory.getId()).get();

        Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
        Assertions.assertEquals(expectedName,actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedActive,actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(),(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.getDeletedAt());
    }


    @Test
    public void giverValidaCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty(){
        final int expectedQuantityRegisterInBankBeforePersist = 0;
        Assertions.assertEquals(expectedQuantityRegisterInBankBeforePersist, categoryRepository.count());
        Optional<Category> actualCategory = categoryMySQLGateway.findById(CategoryID.from("empty"));
         Assertions.assertTrue(actualCategory.isEmpty());
    }


    @Test
    public void givenPrePersistedCategories_whenCallFindAll_shouldReturnPaginated(){
        final int expectedPage = 0;
        final int expectedPerPage = 1;
        final int expectedTotal = 3;
        Category filmes = Category.newCategory("Filmes", null, true);
        Category series = Category.newCategory("Series", null, true);
        Category documentarios = Category.newCategory("Documentarios", null, true);

        Assertions.assertEquals(0,categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJPAEntity.from(filmes),
                CategoryJPAEntity.from(series),
                CategoryJPAEntity.from(documentarios)
        ));

        Assertions.assertEquals(3,categoryRepository.count());
        final CategorySearchQuery query = new CategorySearchQuery(0,1,"","name","asc");
        final Pagination<Category> actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void givenEmptyCategoriesTable_whenCallFindAll_shouldReturnPaginated(){

        final int expectedPage = 0;
        final int expectedPerPage = 1;
        final int expectedTotal = 0;


        Assertions.assertEquals(0,categoryRepository.count());
        final CategorySearchQuery query = new CategorySearchQuery(0,1,"","name","asc");
        final Pagination<Category> actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallFindAllWithPage1_shouldReturnPaginated(){

        int expectedPage = 0;
        final int expectedPerPage = 1;
        final int expectedTotal = 3;
        Category filmes = Category.newCategory("Filmes", null, true);
        Category series = Category.newCategory("Series", null, true);
        Category documentarios = Category.newCategory("Documentarios", null, true);

        Assertions.assertEquals(0,categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJPAEntity.from(filmes),
                CategoryJPAEntity.from(series),
                CategoryJPAEntity.from(documentarios)
        ));

        Assertions.assertEquals(3,categoryRepository.count());

        CategorySearchQuery query = new CategorySearchQuery(0,1,"","name","asc");
        Pagination<Category> actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());

        //Page 1

        expectedPage = 1;
        query = new CategorySearchQuery(1,1,"","name","asc");
        actualResult = categoryMySQLGateway.findAll(query);


        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(filmes.getId(), actualResult.items().get(0).getId());





        //Page 2

        expectedPage = 2;
        query = new CategorySearchQuery(2,1,"","name","asc");
        actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(series.getId(), actualResult.items().get(0).getId());

    }



    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallFindAllAndTermsMachesCategoryName_shouldReturnPaginated(){
        final int expectedPage = 0;
        final int expectedPerPage = 1;
        final int expectedTotal = 1;
        Category filmes = Category.newCategory("Filmes", null, true);
        Category series = Category.newCategory("Series", null, true);
        Category documentarios = Category.newCategory("Documentarios", null, true);

        Assertions.assertEquals(0,categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJPAEntity.from(filmes),
                CategoryJPAEntity.from(series),
                CategoryJPAEntity.from(documentarios)
        ));

        Assertions.assertEquals(3,categoryRepository.count());
        final CategorySearchQuery query = new CategorySearchQuery(0,1,"doc","name","asc");
        final Pagination<Category> actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());

    }


    @Test
    public void givenPrePersistedCategoriesAndMaisAssistidaAsTerms_whenCallFindAllAndTermsMachesCategoryDescription_shouldReturnPaginated(){
        final int expectedPage = 0;
        final int expectedPerPage = 1;
        final int expectedTotal = 1;
        Category filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        Category series = Category.newCategory("Series", "Uma categoria assistida", true);
        Category documentarios = Category.newCategory("Documentarios", "Uma categoria menos assistida", true);

        Assertions.assertEquals(0,categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJPAEntity.from(filmes),
                CategoryJPAEntity.from(series),
                CategoryJPAEntity.from(documentarios)
        ));

        Assertions.assertEquals(3,categoryRepository.count());
        final CategorySearchQuery query = new CategorySearchQuery(0,1,"MAIS assistida","name","asc");
        final Pagination<Category> actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage,actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
        Assertions.assertEquals(expectedTotal,actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(filmes.getId(), actualResult.items().get(0).getId());

    }





}
