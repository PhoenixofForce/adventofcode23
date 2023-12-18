package dev.phoenixofforce.adventofcode.y2023.day06;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.springframework.stereotype.Component;

@Component
public class Day06_2023 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        String[] times = input.getLines().get(0).split(" +");
        String[] distances = input.getLines().get(1).split(" +");

        long out = 1;
        for(int i = 1; i < times.length; i++) {
            out *= getNumberOfWaysToWin(Long.parseLong(times[i]), Long.parseLong(distances[i]));
        }

        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        String times = input.getLines().get(0).replaceAll(" +", "").substring("Time:".length());
        String distances = input.getLines().get(1).replaceAll(" +", "").substring("Distance:".length());

        return  getNumberOfWaysToWin(Long.parseLong(times), Long.parseLong(distances));
    }

    private long getNumberOfWaysToWin(long time, long distance) {
        long ways = 0;
        for(int holdingTime = 0; holdingTime <= time; holdingTime++) {
            long speed = holdingTime;
            long distanceToDrive = speed * (time - holdingTime);
            if(distanceToDrive > distance) ways++;
        }
        return ways;
    }

}
