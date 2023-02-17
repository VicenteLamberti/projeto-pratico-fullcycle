package com.fullcycle.vicente.application.genre.create;

import com.fullcycle.vicente.application.category.create.CreateCategoryCommand;
import com.fullcycle.vicente.application.category.create.CreateCategoryOutput;
import com.fullcycle.vicente.application.category.create.CreateCategoryUseCase;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.NotificationException;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreGateway;
import com.fullcycle.vicente.domain.validation.Error;
import com.fullcycle.vicente.domain.validation.ValidationHandler;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase {


    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public DefaultCreateGenreUseCase(final GenreGateway genreGateway, CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateGenreOutput execute(final CreateGenreCommand aCommand) {
        final String  aName = aCommand.name();
        final boolean isActive = aCommand.isActive();
        final List<CategoryID> categories = toCategoryID(aCommand.categories());

        final Notification notification = Notification.create();
        notification.append(validateCategories(categories));
        final Genre aGenre = notification.validate(()-> Genre.newGenre(aName,isActive));
        if(notification.hasError()){
            throw new NotificationException("Could not create Aggregate Genre", notification);
        }
        aGenre.addCategories(categories);
        return CreateGenreOutput.from(this.genreGateway.create(aGenre));
    }

    private ValidationHandler validateCategories(List<CategoryID> ids) {
        final Notification notification = Notification.create();
        if(ids == null || ids.isEmpty()){
            return notification;
        }

        final List<CategoryID> retriviedIds = categoryGateway.existsByIds(ids);
        if(ids.size() != retriviedIds.size()){
            final List<CategoryID> missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retriviedIds);

            final String missingIdsMessaage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", ")) ;

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessaage)));
        }
        return  notification;
    }

    private List<CategoryID> toCategoryID(final List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }

    private Either<Notification, CreateCategoryOutput> create(final Category aCategory){
        return API.Try(()->this.categoryGateway.create(aCategory))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }
}
