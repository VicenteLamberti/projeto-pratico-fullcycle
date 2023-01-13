package com.fullcycle.vicente.application.category.create;

import com.fullcycle.vicente.application.UseCase;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification,CreateCategoryOutput>>{
}
