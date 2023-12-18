package dev.phoenixofforce.adventofcode.y2023.day04;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Day04_2023 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        return input.getLines().stream()
            .mapToLong(this::getMatchingNumberCount)
            .map(this::getPoints)
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        long[] wins = new long[input.getLines().size()];
        Arrays.fill(wins, 1);

        for(int i = 0; i < wins.length; i++) {
            long matchingNumbers = getMatchingNumberCount(input.getLines().get(i));
            for(long reward = i + 1; reward <= i + matchingNumbers; reward++) {
                if(reward < input.getLines().size()) {
                    if(reward < wins.length)
                        wins[(int) reward] += wins[i];
                }
            }
        }

        return Arrays.stream(wins).sum();
    }

    public long getMatchingNumberCount(String line) {
        String winningNumbersString = line.split(":")[1].trim().split("\\|")[0].trim().replaceAll(" +", " ");
        String yourNumbersString = line.split("\\|")[1].trim().replaceAll(" +", " ");;

        List<Integer> winningNumbers = Arrays.stream(winningNumbersString.split(" ")).map(Integer::parseInt).toList();
        List<Integer> yourNumbers = Arrays.stream(yourNumbersString.split(" ")).map(Integer::parseInt).toList();

        return yourNumbers.stream()
            .filter(winningNumbers::contains)
            .count();
    }

    public long getPoints(long matchingCount) {
        return matchingCount == 0? 0: (long) Math.pow(2, matchingCount - 1);
    }

}
