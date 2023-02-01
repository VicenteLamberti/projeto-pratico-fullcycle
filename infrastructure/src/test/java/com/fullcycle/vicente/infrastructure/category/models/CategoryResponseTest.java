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
public class CategoryResponseTest {

    @Autowired
    private JacksonTester<CategoryResponse> json;

    @Test
    public void testMarshall() throws IOException {
        final String expectedId= "123";
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedActive = true;
        final Instant expectedCreatedAt = Instant.now();
        final Instant expectedUpdatedAt = Instant.now();
        final Instant expectedDeletedAt = Instant.now();

        final CategoryResponse response = new CategoryResponse(
                expectedId,
                expectedName,
                expectedDescription,
                expectedActive,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );
        final JsonContent<CategoryResponse> actualJson = this.json.write(response);
        Assertions.assertThat(actualJson)
                .hasJsonPath("$.id", expectedId)
                .hasJsonPath("$.name", expectedName)
                .hasJsonPath("$.description", expectedDescription)
                .hasJsonPath("$.created_at", expectedCreatedAt.toString())
                .hasJsonPath("$.updated_at", expectedUpdatedAt.toString())
                .hasJsonPath("$.deleted_at", expectedDeletedAt.toString());
    }

    @Test
    public void testUnmarshall() throws IOException {
        final String expectedId= "123";
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedActive = true;
        final Instant expectedCreatedAt = Instant.now();
        final Instant expectedUpdatedAt = Instant.now();
        final Instant expectedDeletedAt = Instant.now();

        final String json = """
                {
                    "id":"%s",
                    "name":"%s",
                    "description":"%s",
                    "is_active":"%s",
                    "created_at":"%s",
                    "updated_at":"%s",
                    "deleted_at":"%s"
                }
                """.formatted(expectedId, expectedName,expectedDescription,expectedActive,expectedCreatedAt.toString(),expectedUpdatedAt.toString(),expectedDeletedAt.toString());

        final ObjectContent<CategoryResponse> actualJson = this.json.parse(json);
        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedActive)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
                .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
    }
}
