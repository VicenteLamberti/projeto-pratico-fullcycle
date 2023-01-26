package com.fullcycle.vicente.infrastructure.api.controllers;

import com.fullcycle.vicente.application.category.create.CreateCategoryCommand;
import com.fullcycle.vicente.application.category.create.CreateCategoryOutput;
import com.fullcycle.vicente.application.category.create.CreateCategoryUseCase;
import com.fullcycle.vicente.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.vicente.domain.pagination.Pagination;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import com.fullcycle.vicente.infrastructure.api.CategoryAPI;
import com.fullcycle.vicente.infrastructure.category.models.CategoryApiOutput;
import com.fullcycle.vicente.infrastructure.category.models.CreateCategoryApiInput;
import com.fullcycle.vicente.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoryByIdUseCase getCategoryByIdUseCase) {

        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;

    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {

       final CreateCategoryCommand aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

       final Function<Notification, ResponseEntity<?>> onError = notification ->
               ResponseEntity.unprocessableEntity().body(notification);

       final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess =  output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand).fold(onError,onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public CategoryApiOutput getById(String id) {
        //Assim ou
//        return CategoryApiPresenter.present.apply(this.getCategoryByIdUseCase.execute(id));
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));

    }
}
