package com.fullcycle.vicente.infrastructure.genre.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class GenreCategoryID {
    @Column(name = "genre_id", nullable = false)
    private String genreId;

    @Column(name = "category_id",nullable = false)
    private String categoryId;

    public GenreCategoryID() {

    }
    private GenreCategoryID (String genreId, String categoryId) {
        this.genreId = genreId;
        this.categoryId = categoryId;
    }

    public static GenreCategoryID from(String genreId, String categoryId) {
       return new GenreCategoryID(genreId,categoryId);
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreCategoryID that = (GenreCategoryID) o;
        return Objects.equals(getGenreId(), that.getGenreId()) && Objects.equals(getCategoryId(), that.getCategoryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGenreId(), getCategoryId());
    }
}
