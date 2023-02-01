package com.fullcycle.vicente.infrastructure.api.controllers;

import com.fullcycle.vicente.application.category.create.CreateCategoryCommand;
import com.fullcycle.vicente.application.category.create.CreateCategoryOutput;
import com.fullcycle.vicente.application.category.create.CreateCategoryUseCase;
import com.fullcycle.vicente.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.vicente.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.vicente.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.vicente.application.category.update.UpdateCategoryCommand;
import com.fullcycle.vicente.application.category.update.UpdateCategoryOutput;
import com.fullcycle.vicente.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.vicente.domain.category.CategorySearchQuery;
import com.fullcycle.vicente.domain.pagination.Pagination;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import com.fullcycle.vicente.infrastructure.api.CategoryAPI;
import com.fullcycle.vicente.infrastructure.category.models.CategoryListResponse;
import com.fullcycle.vicente.infrastructure.category.models.CategoryResponse;
import com.fullcycle.vicente.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.vicente.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.vicente.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    private final UpdateCategoryUseCase updateCategoryUseCase;

    private final DeleteCategoryUseCase deleteCategoryUseCase;

    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoryByIdUseCase getCategoryByIdUseCase,
                              UpdateCategoryUseCase updateCategoryUseCase,DeleteCategoryUseCase deleteCategoryUseCase,
                              ListCategoriesUseCase listCategoriesUseCase) {

        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase =  Objects.requireNonNull(listCategoriesUseCase);

    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {

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
    public Pagination<CategoryListResponse> listCategories(final String search, final int page, final int perPage, final String sort, final String direction) {
        return listCategoriesUseCase.execute(new CategorySearchQuery(page,perPage,search,sort,direction))
                .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(String id) {
        //Assim ou
//        return CategoryApiPresenter.present.apply(this.getCategoryByIdUseCase.execute(id));
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));

    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateCategoryRequest input) {
        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.ok(output);

        return this.updateCategoryUseCase.execute(aCommand).fold(onError,onSuccess);
    }

    @Override
    public void delete(String id) {
        this.deleteCategoryUseCase.execute(id);
    }




}
