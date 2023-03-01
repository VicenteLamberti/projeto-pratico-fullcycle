package com.fullcycle.vicente.application.genre.update;

import com.fullcycle.vicente.application.category.update.DefaultUpdateCategoryUseCase;
import com.fullcycle.vicente.application.category.update.UpdateCategoryCommand;
import com.fullcycle.vicente.application.category.update.UpdateCategoryOutput;
import com.fullcycle.vicente.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.vicente.domain.Identifier;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import com.fullcycle.vicente.domain.category.CategoryID;
import com.fullcycle.vicente.domain.exceptions.NotFoundException;
import com.fullcycle.vicente.domain.exceptions.NotificationException;
import com.fullcycle.vicente.domain.genre.Genre;
import com.fullcycle.vicente.domain.genre.GenreGateway;
import com.fullcycle.vicente.domain.genre.GenreID;
import com.fullcycle.vicente.domain.validation.Error;
import com.fullcycle.vicente.domain.validation.ValidationHandler;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

    public DefaultUpdateGenreUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway){
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }
    @Override
    public UpdateGenreOutput execute(UpdateGenreCommand aCommand) {
        final GenreID anId = GenreID.from(aCommand.id());
        final String  aName = aCommand.name();
        final boolean isActive = aCommand.isActive();
        final List<CategoryID> categories = toCategoryID(aCommand.categories());

        final Genre aGenre = this.genreGateway.findById(anId).orElseThrow(notFound(anId));


        final Notification notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(()->aGenre.update(aName,isActive,categories));
        if(notification.hasError()){
            throw new NotificationException("Could not update Aggregate Genre", notification);
        }


        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
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

    private Supplier<NotFoundException> notFound(Identifier anId){
        return ()-> NotFoundException.with(
                Category.class,anId
        );
    }
}
