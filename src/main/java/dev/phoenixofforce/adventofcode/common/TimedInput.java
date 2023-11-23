package dev.phoenixofforce.adventofcode.common;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;

@Slf4j
public class TimedInput {

    private static final ExecutorService l = Executors.newFixedThreadPool(1);

    //https://stackoverflow.com/questions/61807890/user-input-with-a-timeout-in-java
    public static String getChoiceWithTimeout(int timeOutInSeconds) {
        Callable<String> callable = () -> new BufferedReader(new InputStreamReader(System.in)).readLine();

        LocalDateTime start = LocalDateTime.now();
        Future<String> g = l.submit(callable);
        while (ChronoUnit.SECONDS.between(start, LocalDateTime.now()) < timeOutInSeconds) {
            if (g.isDone()) {
                try {
                    return g.get();
                } catch (InterruptedException | ExecutionException | IllegalArgumentException e) {
                    g = l.submit(callable);
                }
            }
        }
        g.cancel(true);
        return null;
    }

}
