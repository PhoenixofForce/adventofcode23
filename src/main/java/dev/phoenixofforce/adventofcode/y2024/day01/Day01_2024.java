package dev.phoenixofforce.adventofcode.y2024.day01;

import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Day01_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<List<Long>> lines = input.getLines()
            .stream()
            .map(lineEntry -> Arrays.stream(lineEntry.split(" +")))
            .map(arrayStreamEntry -> arrayStreamEntry.map(Long::parseLong)
                .toList()
            ).toList();

        List<Long> firstList = lines.stream()
            .map(e -> e.get(0))
            .sorted()
            .toList();

        List<Long> secondList = lines.stream()
            .map(e -> e.get(1))
            .sorted()
            .toList();

        return IntStream.range(0, firstList.size())
            .mapToLong(i -> Math.abs(firstList.get(i) - secondList.get(i)))
            .peek(System.out::println)
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<List<Long>> lines = input.getLines()
            .stream()
            .map(lineEntry -> Arrays.stream(lineEntry.split(" +")))
            .map(arrayStreamEntry -> arrayStreamEntry.map(Long::parseLong)
                .toList()
            ).toList();

        List<Long> firstList = lines.stream()
            .map(e -> e.get(0))
            .sorted()
            .toList();

        List<Long> secondList = lines.stream()
            .map(e -> e.get(1))
            .sorted()
            .toList();

        return firstList.stream().mapToLong(number -> number * secondList.stream().filter(e -> Objects.equals(e, number)).count())
            .peek(System.out::println)
            .sum();
    }

}
