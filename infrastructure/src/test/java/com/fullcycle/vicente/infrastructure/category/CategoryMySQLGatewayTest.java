package com.fullcycle.vicente.infrastructure.category;

import com.fullcycle.vicente.infrastructure.MySQLGatewayTest;
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
    public void tesInjectedDependencies(){
        Assertions.assertNotNull(categoryRepository);
        Assertions.assertNotNull(categoryMySQLGateway);
    }




}
