package dev.phoenixofforce.adventofcode.y2025.day06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Day06_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        long out = 0;
        for(int col = 0; col < input.getLines().getFirst().trim().split("\\s+").length; col++) {
            List<Long> numbers = new ArrayList<>();
            for(int row = 0; row < input.height() - 1; row++) {
                String part = input.getLines().get(row).trim().split("\\s+")[col];
                numbers.add(Long.parseLong(part));
            }
            char operator = input.getLines().get((int) (input.height() - 1)).trim().split("\\s+")[col].charAt(0);
            long result = numbers.stream().reduce(operator == '*' ? 1L : 0L, (a, b) -> operator == '*' ? a * b : a + b);
            out += result;
        }
        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<String> columns = new ArrayList<>();
        for(int col = 0; col < input.width(); col++) {
            String column = "";
            for(int row = 0; row < input.height(); row++) {
                column += input.getLines().get(row).charAt(col);
            }
            if(column.contains("*")) {
                columns.add("*");
                columns.add(column.replace("*", "").trim());
            } else if(column.contains("+")) {
                columns.add("+");
                columns.add(column.replace("+", "").trim());
            } else {
                columns.add(column.trim());
            }
        }
        columns.add("");
        log.info("{}", columns);

        long out = 0;

        char operator = '*';
        long currentValue = 0;
        String debugOperation = "";
        for(String column: columns) {
            switch (column) {
                case "*", "+" -> {
                    operator = column.charAt(0);
                    currentValue = operator == '*' ? 1L : 0L;
                }
                case "" -> {
                    log.info("{}\t = {}", currentValue, debugOperation.substring(0, debugOperation.length() - 3));
                    out += currentValue;
                    currentValue = 0;
                    debugOperation = "";
                }
                default -> {
                    long columnValue = Long.parseLong(column);
                    currentValue = operator == '*' ? currentValue * columnValue : currentValue + columnValue;
                    debugOperation += column + " " + operator + " ";
                }
            }
        }
        return out;
    }

}
