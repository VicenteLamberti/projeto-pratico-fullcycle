package com.fullcycle.vicente.infrastructure.category;

import com.fullcycle.vicente.domain.category.Category;
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




}
