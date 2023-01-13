package com.fullcycle.vicente.application.category.update;

import com.fullcycle.vicente.application.UseCase;
import com.fullcycle.vicente.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification,UpdateCategoryOutput>> {
}
