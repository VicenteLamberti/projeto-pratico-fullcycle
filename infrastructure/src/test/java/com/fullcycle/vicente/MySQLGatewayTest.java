package com.fullcycle.vicente;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

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
        @ComponentScan.Filter(type = FilterType.REGEX,pattern = ".[MySQLGateway]")
})
@ExtendWith(MySQLCleanUpExtension.class)
public @interface MySQLGatewayTest {



}
