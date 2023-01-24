package com.fullcycle.vicente.application.category.retrieve.list;

import com.fullcycle.vicente.IntegrationTest;
import com.fullcycle.vicente.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategorySearchQuery;
import com.fullcycle.vicente.domain.pagination.Pagination;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;


@IntegrationTest
public class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @BeforeEach
    void mockUp() {
        List<CategoryJPAEntity> categories = List.of(
                        Category.newCategory("Filmes", null, true),
                        Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix", true),
                        Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon Prime", true),
                        Category.newCategory("Documentários", null, true),
                        Category.newCategory("Sports", null, true),
                        Category.newCategory("Kids", "Categoria para crianças", true),
                        Category.newCategory("Series", null, true)
                )
                .stream().map(CategoryJPAEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenAValidTerm_WhenDoesntMatchesPrePersisted_shouldReturnEmptyPage(){
        final int expectedPage = 0;

        final int expectedPerPage=10;

        final String expectedTerms = "FAILL";
        final String expectedSort = "name";
        final String expectedDirection = "asc";
        final int expectedItemsCount = 0;
        final int expectedTotal = 0;

        
        final CategorySearchQuery aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);

        Pagination<CategoryListOutput> actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "KI,0,10,1,1,Kids",
            "crianças,0,10,1,1,Kids",
            "da Amazon,0,10,1,1,Amazon Originals"
    })
    public void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedCategoryName
    ){

        final String expectedSort = "name";
        final String expectedDirection = "asc";



        final CategorySearchQuery aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);

        Pagination<CategoryListOutput> actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals",
            "name,desc, 0,10,7,7,Sports",
            "createdAt,asc, 0,10,7,7,Filmes",
//            "createdAt,desc, 0,10,7,7,Series" Por causa do Instant
    })
    public void givenAValidSortAndDirection_whenCallListCategories_shouldReturnCategoriesOrderedBySortAndDirection(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedCategoryName
    ){

        final CategorySearchQuery aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,"",expectedSort,expectedDirection);

        Pagination<CategoryListOutput> actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }


    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon Originals;Documentários",
            "1,2,2,7,Filmes;Kids",
            "2,2,2,7,Netflix Originals;Series",
            "3,2,1,7,Sports",
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (final String expectedName : expectedCategoriesName.split(";")) {
            final String actualName = actualResult.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }


//    @Test
//    public void givenAValidQuery_whenCallListCategories_thenShouldReturnCategories(){
//
//
//        final int expectedPage = 0;
//
//        final int expectedPerPage=10;
//
//        final String expectedTerms = "";
//        final String expectedSort = "createdAt";
//        final String expectedDirection = "asc";
//        final CategorySearchQuery aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms,expectedSort,expectedDirection);
//        final Pagination<Category> expectedPagination = new Pagination<Category>(expectedPage,expectedPerPage,categories.size(),categories);
//        final int expectedItemsCount=2;
//        final Pagination<CategoryListOutput> expectedResult = expectedPagination.map(CategoryListOutput::from);
//
//
//        final var actualResult = useCase.execute(aQuery);
//
//        Assertions.assertEquals(expectedItemsCount,actualResult.items().size());
////        Assertions.assertEquals(expectedResult,actualResult);
//        Assertions.assertEquals(expectedPerPage,actualResult.perPage());
//        Assertions.assertEquals(expectedPage,actualResult.currentPage());
//        Assertions.assertEquals(categories.size(),actualResult.total());
//
//    }


    private void save(final Category... aCategory) {

        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJPAEntity::from)
                        .toList()
        );
    }
}
