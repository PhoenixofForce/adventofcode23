package dev.phoenixofforce.adventofcode.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.*;

@Slf4j
public class TimedInput {

    //https://stackoverflow.com/questions/61807890/user-input-with-a-timeout-in-java
    public static String getChoiceWithTimeout(int timeOutInSeconds) {
        Callable<String> k = () -> new Scanner(System.in).nextLine();
        long start = System.currentTimeMillis();
        String choice = "";
        boolean valid;
        ExecutorService l = Executors.newFixedThreadPool(1);
        Future<String> g;
        g = l.submit(k);

        done: while (System.currentTimeMillis() - start < timeOutInSeconds * 1000) {
            do {
                valid = true;
                if (g.isDone()) {
                    try {
                        choice = g.get();
                        if (!choice.isEmpty()) {
                            break done;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    } catch (InterruptedException | ExecutionException | IllegalArgumentException e) {
                        valid = false;
                    }
                }
            } while (!valid);
        }

        l.close();
        g.cancel(true);
        return choice;
    }

}
