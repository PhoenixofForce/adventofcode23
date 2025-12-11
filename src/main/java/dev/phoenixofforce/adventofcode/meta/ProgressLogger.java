package dev.phoenixofforce.adventofcode.meta;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProgressLogger {

    private static long firstUpdate = 0;

    public static void log(long max, long current, Object value) {
        if(firstUpdate == 0) firstUpdate = System.nanoTime();
        long delta = System.nanoTime() - firstUpdate;
        double speed = ((double) delta / current) * 0.000001;

        double percent = 100.0 * current / max;
        String percentFormat = String.format("%.2f", percent);

        double timeLeft = speed * (max - current);
        String timeFormat = TimeUtil.parseTime(timeLeft);
        log.info("{}% : {} / {} ({} left) = {}", percentFormat, current, max, timeFormat, value);
    }

}
