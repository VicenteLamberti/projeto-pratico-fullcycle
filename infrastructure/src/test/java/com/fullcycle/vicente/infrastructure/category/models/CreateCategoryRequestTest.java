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

import static org.junit.jupiter.api.Assertions.*;


@JacksonTest
public class CreateCategoryRequestTest {

    @Autowired
    private JacksonTester<CreateCategoryRequest> json;


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

        final ObjectContent<CreateCategoryRequest> actualJson = this.json.parse(json);
        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedActive);
    }

    @Test
    public void testMarshall() throws IOException {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedActive = true;


        final CreateCategoryRequest response = new CreateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedActive
        );
        final JsonContent<CreateCategoryRequest> actualJson = this.json.write(response);
        Assertions.assertThat(actualJson)

                .hasJsonPath("$.name", expectedName)
                .hasJsonPath("$.description", expectedDescription)
                .hasJsonPath("$.is_active", expectedActive);

    }
}