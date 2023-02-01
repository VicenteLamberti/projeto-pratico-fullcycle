package com.fullcycle.vicente.infrastructure.category.models;

import com.fullcycle.vicente.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.time.Instant;

@JacksonTest
public class UpdateCategoryRequestTest {

    @Autowired
    private JacksonTester<UpdateCategoryRequest> json;


    @Test
    public void testUnmarshall() throws IOException {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedActive = true;


        final String json = """
                {
                    "name":"%s",
                    "description":"%s",
                    "is_active":"%s"
                }
                """.formatted(expectedName,expectedDescription,expectedActive);

        final ObjectContent<UpdateCategoryRequest> actualJson = this.json.parse(json);
        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedActive);
    }
}
