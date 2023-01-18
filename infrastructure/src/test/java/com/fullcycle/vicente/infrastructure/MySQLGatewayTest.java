package com.fullcycle.vicente.infrastructure;


import com.fullcycle.vicente.infrastructure.category.CategoryMySQLGatewayTest;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

/**
 * Foi necessário adicionar esse ComponentScan, pois Utilizando o DataJpaTest, ele não consegue enxergar o service CategoryMySQLGateway
 * Se fosse o SprintBootTest, funcionaria
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@DataJpaTest
@ActiveProfiles("test")
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX,pattern = ".*[MySQLGateway]")
})
@ExtendWith(MySQLGatewayTest.CleanUpExtensions.class)
public @interface MySQLGatewayTest {

    /**
     * Essa classe foi criada pra auxiliar na limpeza antes dos testes, é importado com
     * @ExtendWith(CategoryMySQLGatewayTest.CleanUpExtensions.class)
     */
    class  CleanUpExtensions implements BeforeEachCallback {
        @Override
        public void beforeEach(ExtensionContext context)  {
            final Collection<CrudRepository> repositories = SpringExtension
                    .getApplicationContext(context)
                    .getBeansOfType(CrudRepository.class)
                    .values();

            cleanUp(repositories);
        }
        private void cleanUp(Collection<CrudRepository> repositories) {
            repositories.forEach(CrudRepository::deleteAll);
        }
    }

}
