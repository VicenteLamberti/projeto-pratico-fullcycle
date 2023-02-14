package com.fullcycle.vicente.domain.genre;

import com.fullcycle.vicente.domain.AggregateRoot;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.NotificationException;
import com.fullcycle.vicente.domain.utils.InstantUtils;
import com.fullcycle.vicente.domain.validation.ValidationHandler;
import com.fullcycle.vicente.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> implements Cloneable {
    private String name;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private List<CategoryID> categories;

    private Genre(final GenreID anId, final String aName, final boolean isActive, final Instant createdAt, final Instant updatedAt, final Instant deletedAt,final List<CategoryID>categories){
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.categories = categories;

        Notification notification = Notification.create();
        validate(notification);
        if(notification.hasError()){
            throw new NotificationException("Failed to create a Aggregate Genre",notification);
        }
    }

    public static Genre newGenre(final String aName, final boolean isActive){
        final GenreID id = GenreID.unique();
        final Instant now = InstantUtils.now();
        final Instant deletedAt = isActive ? null : now;
        return new Genre(id,aName, isActive, now, now , deletedAt, new ArrayList<>());
    }

    public static Genre clone(final Genre aGenre){
        return aGenre.clone();
    }

    public static Genre with(final Genre aGenre){
        return with(
                aGenre.id,
                aGenre.name,
                aGenre.active,
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt,
                aGenre.categories

        );
    }

    public static Genre with(
            final GenreID anId,
            final String name,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final List<CategoryID> categories){
        return new Genre(
                anId,
                name,
                active,
                createdAt,
                updatedAt,
                deletedAt,
                categories
        );
    }


    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public List<CategoryID> getCategories(){
        return Collections.unmodifiableList(categories);
    }

    @Override
    public Genre clone() {
        try{
            return (Genre) super.clone();
        }catch (CloneNotSupportedException ex){
            throw  new AssertionError();
        }
    }
    @Override
    public void validate(final ValidationHandler handler) {

        new GenreValidator(this,handler).validate();
    }

    public Genre deactivate(){
        if(getDeletedAt() == null){
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre activate(){
        this.active = true;
        this.deletedAt = null;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre update(final String aName, final boolean isActive){
        this.name = aName;
        if(isActive){
            activate();
        }
        else {
            deactivate();
        }
        this.updatedAt = InstantUtils.now();
        return  this;
    }


}
