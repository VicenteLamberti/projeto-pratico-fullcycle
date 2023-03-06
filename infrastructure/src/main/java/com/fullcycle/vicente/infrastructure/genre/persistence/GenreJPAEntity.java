package com.fullcycle.vicente.infrastructure.genre.persistence;

import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreID;
import jakarta.persistence.*;

import java.awt.geom.GeneralPath;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="genres")
public class GenreJPAEntity {

    @Column(name="id",nullable = false)
    private String id;
    @Column(name="name",nullable = false)
    private String name;
    @Column(name="active",nullable = false)
    private boolean active;
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private Set<GenreCategoryJPAEntity> categories;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;
    @Column(name = "deleted_at",  columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public GenreJPAEntity() {
    }

    private GenreJPAEntity(String id, String name, boolean active, Instant createdAt, Instant updatedAt, Instant deletedAt) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.categories = new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static GenreJPAEntity from(final Genre aGenre){
        final GenreJPAEntity anEntity = new GenreJPAEntity(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
        );
        aGenre.getCategories().forEach((anEntity::addCategory));
        return anEntity;
    }

    public Genre toAggregate(){
        return Genre.with(
                GenreID.from(getId()),
                getName(),
                isActive(),
                getCreatedAt(),
                getUpdatedAt(),
                getDeletedAt(),
                getCategories().stream()
                        .map(it->CategoryID.from(it.getId().getCategoryId()))
                        .toList()
        );
    }

    private void addCategory(final CategoryID anId){
        this.categories.add(GenreCategoryJPAEntity.from(this,anId));
    }

    private void removeCategory(final CategoryID anId){
        this.categories.remove(GenreCategoryJPAEntity.from(this,anId));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<GenreCategoryJPAEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<GenreCategoryJPAEntity> categories) {
        this.categories = categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
