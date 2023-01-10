package com.fullcycle.vicente.infrastructure;

import com.fullcycle.vicente.application.UseCase;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        System.out.println(new UseCase().execute());
    }
}