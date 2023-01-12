package com.fullcycle.vicente.application;

import com.fullcycle.vicente.domain.category.Category;

public abstract class UseCase<IN,OUT> {
    public abstract OUT execute(IN anIn);
}