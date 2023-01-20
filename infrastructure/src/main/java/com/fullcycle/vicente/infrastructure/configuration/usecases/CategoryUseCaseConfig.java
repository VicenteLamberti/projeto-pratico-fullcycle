package com.fullcycle.vicente.infrastructure.configuration.usecases;

import com.fullcycle.vicente.application.category.create.CreateCategoryUseCase;
import com.fullcycle.vicente.application.category.create.DefaultCreateCategoryUseCase;
import com.fullcycle.vicente.application.category.delete.DefaultDeleteCategoryUseCase;
import com.fullcycle.vicente.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.vicente.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.fullcycle.vicente.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.vicente.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.fullcycle.vicente.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.vicente.application.category.update.DefaultUpdateCategoryUseCase;
import com.fullcycle.vicente.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.vicente.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    //Assim é pra injetar da forma raiz, senão utilizar a dependencia do javax inject, e anotar o DefaultCreateCategoryUseCase com @named
    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(){
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase(){
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase(){
        return new DefaultListCategoriesUseCase(categoryGateway);
    }
}
