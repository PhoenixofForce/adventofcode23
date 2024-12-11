package dev.phoenixofforce.adventofcode.y2024.day11;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day11_2024 implements Puzzle {

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class CacheItem {
        long stoneValue;
        int iteration;
        int maxIteration;
    }
    private final Map<CacheItem, Long> resultCache = new HashMap<>();

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Long> stones = Arrays.stream(input.getFile().split(" ")).map(Long::parseLong).toList();
        return stones.stream()
            .mapToLong(
                s -> getSize(s, 0, 25)
            ).sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<Long> stones = Arrays.stream(input.getFile().split(" ")).map(Long::parseLong).toList();
        return stones.stream()
            .mapToLong(
                s -> getSize(s, 0, 75)
            ).sum();
    }

    private long getSize(long stone, int iteration, int maxIteration) {
        if(resultCache.containsKey(new CacheItem(stone, iteration, maxIteration))) return resultCache.get(new CacheItem(stone, iteration, maxIteration));
        if(iteration >= maxIteration) return 1;

        if(stone == 0) {
            long result = getSize(1, iteration + 1, maxIteration);
            resultCache.put(new CacheItem(stone, iteration, maxIteration), result);
            return result;
        }

        String s = stone + "";
        if(s.length() % 2 == 0) {
            String left = s.substring(0, s.length() / 2);
            String right = s.substring(s.length() / 2);
            long result = getSize(Long.parseLong(left), iteration + 1, maxIteration) + getSize(Long.parseLong(right), iteration + 1, maxIteration);
            resultCache.put(new CacheItem(stone, iteration, maxIteration), result);
            return result;
        }

        long result = getSize(stone * 2024L, iteration + 1, maxIteration);
        resultCache.put(new CacheItem(stone, iteration, maxIteration), result);
        return result;
    }
}
