package com.fullcycle.vicente.infrastructure.category.persistence;

import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAInvalidNameNull_whenCallsSave_shouldReturnError(){
        final String expectedProperty = "name";
        final String expectedCauseErrorMessage = "not-null property references a null or transient value : com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity.name";
        final Category aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final CategoryJPAEntity aEntity = CategoryJPAEntity.from(aCategory);
        aEntity.setName(null);

        DataIntegrityViolationException actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(aEntity));

        PropertyValueException actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedCauseErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAInvalidCreateAtNull_whenCallsSave_shouldReturnError(){
        final String expectedProperty = "createdAt";
        final String expectedCauseErrorMessage = "not-null property references a null or transient value : com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity.createdAt";
        final Category aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final CategoryJPAEntity aEntity = CategoryJPAEntity.from(aCategory);
        aEntity.setCreatedAt(null);

        DataIntegrityViolationException actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(aEntity));

        PropertyValueException actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedCauseErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAInvalidUpdatedAtNull_whenCallsSave_shouldReturnError(){
        final String expectedProperty = "updatedAt";
        final String expectedCauseErrorMessage = "not-null property references a null or transient value : com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity.updatedAt";
        final Category aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final CategoryJPAEntity aEntity = CategoryJPAEntity.from(aCategory);
        aEntity.setUpdatedAt(null);

        DataIntegrityViolationException actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(aEntity));

        PropertyValueException actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedProperty, actualCause.getPropertyName());
        Assertions.assertEquals(expectedCauseErrorMessage, actualCause.getMessage());
    }


}
