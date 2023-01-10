package com.fullcycle.vicente.application;

import com.fullcycle.vicente.domain.category.Category;

public class UseCase {
    public Category execute(){
        return Category.newCategory("Nome Categoria","Descrição", true);
    }
}