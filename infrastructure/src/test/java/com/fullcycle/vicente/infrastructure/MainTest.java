package com.fullcycle.vicente.infrastructure;

import com.fullcycle.vicente.application.UseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    public void mainTest(){
        Assertions.assertNotNull(new Main());
        Main.main(new String[]{});
    }
}
