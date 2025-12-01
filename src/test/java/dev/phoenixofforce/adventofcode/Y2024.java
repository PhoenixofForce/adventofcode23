package dev.phoenixofforce.adventofcode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"java.awt.headless=false", "skipAutoRun=true"})
public class Y2024 {

    private final AdventofcodeApplication app;

    @Autowired
    public Y2024(AdventofcodeApplication app) {
        this.app = app;
    }

    @Test
    void run2024() {
        app.solveYear(2024);
    }

}
