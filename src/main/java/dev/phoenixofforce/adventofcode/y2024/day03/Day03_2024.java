package dev.phoenixofforce.adventofcode.y2024.day03;

import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Day03_2024 implements Puzzle {

    private static final String MUL_REGEX = "mul\\((\\d|\\d\\d|\\d\\d\\d),(\\d|\\d\\d|\\d\\d\\d)\\)";
    private static final String DONT_MATCHER = "don't\\(\\)";
    private static final String DO_MATCHER = "do\\(\\)";


    @Override
    public Object solvePart1(PuzzleInput input) {
        List<String> operations = new ArrayList<>();
        Matcher matcher = Pattern.compile(MUL_REGEX)
            .matcher(input.getFile());

        while(matcher.find()) {
            operations.add(matcher.group());
        }

        return operations.stream()
            .mapToLong(this::handleOperations)
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<String> operations = new ArrayList<>();
        Matcher matcher = Pattern.compile("(%s|%s|%s)".formatted(DONT_MATCHER, DO_MATCHER, MUL_REGEX))
            .matcher(input.getFile());

        while(matcher.find()) {
            operations.add(matcher.group());
        }

        long out = 0;
        boolean enabled = true;
        for(String operation: operations) {
            if(operation.startsWith("do(")) enabled = true;
            else if (operation.startsWith("don")) enabled = false;
            else if(enabled) out += handleOperations(operation);
        }

        return out;
    }

    public long handleOperations(String operation) {
        System.out.println(operation);
        String numbersString = operation.replaceAll("[()mul ]", "");
        String[] numbers = numbersString.split(",");
        return Long.parseLong(numbers[0]) * Long.parseLong(numbers[1]);
    }

}
