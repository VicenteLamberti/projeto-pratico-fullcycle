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
public class CategoryListResponseTest {

    @Autowired
    private JacksonTester<CategoryListResponse> json;


    @Test
    public void testMarshall() throws IOException {
        final String expectedId= "123";
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedActive = true;
        final Instant expectedCreatedAt = Instant.now();
        final Instant expectedUpdatedAt = Instant.now();
        final Instant expectedDeletedAt = Instant.now();

        final CategoryListResponse response = new CategoryListResponse(
                expectedId,
                expectedName,
                expectedDescription,
                expectedActive,
                expectedCreatedAt,
                expectedDeletedAt
        );
        final JsonContent<CategoryListResponse> actualJson = this.json.write(response);
        Assertions.assertThat(actualJson)
                .hasJsonPath("$.id", expectedId)
                .hasJsonPath("$.name", expectedName)
                .hasJsonPath("$.description", expectedDescription)
                .hasJsonPath("$.created_at", expectedCreatedAt.toString())
                .hasJsonPath("$.deleted_at", expectedDeletedAt.toString());
    }
}
