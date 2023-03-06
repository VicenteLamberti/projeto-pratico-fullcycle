package com.fullcycle.vicente.infrastructure.genre.persistence;

import com.fullcycle.vicente.domain.category.CategoryID;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "genres_categories")
public class GenreCategoryJPAEntity {

    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @MapsId("genreId")
    private GenreJPAEntity genre;

    public GenreCategoryJPAEntity() {
    }

    private GenreCategoryJPAEntity(final GenreJPAEntity aGenre, CategoryID aCategoryId) {
        this.id = GenreCategoryID.from(aGenre.getId(),aCategoryId.getValue());
        this.genre = genre;
    }

    public static GenreCategoryJPAEntity from(final GenreJPAEntity aGenre, final CategoryID aCategoryId){
        return new GenreCategoryJPAEntity(aGenre,aCategoryId);
    }

    public GenreCategoryID getId() {
        return id;
    }

    public void setId(GenreCategoryID id) {
        this.id = id;
    }

    public GenreJPAEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreJPAEntity genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreCategoryJPAEntity that = (GenreCategoryJPAEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
