package com.fullcycle.vicente.application.genre.create;

import com.fullcycle.vicente.application.UseCase;
import com.fullcycle.vicente.application.category.create.CreateCategoryCommand;
import com.fullcycle.vicente.application.category.create.CreateCategoryOutput;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateGenreUseCase extends UseCase<CreateGenreCommand, CreateGenreOutput>{
}
