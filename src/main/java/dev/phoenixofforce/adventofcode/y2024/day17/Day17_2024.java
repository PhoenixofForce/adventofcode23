package dev.phoenixofforce.adventofcode.y2024.day17;

import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day17_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        long a = Long.parseLong(input.getLines().get(0).split(" ")[2].trim());
        long b = Long.parseLong(input.getLines().get(1).split(" ")[2].trim());
        long c = Long.parseLong(input.getLines().get(2).split(" ")[2].trim());

        String command = input.getLines().get(4).split(" ")[1];
        return runCommand(command, a, b, c);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        String command = input.getLines().get(4).split(" ")[1];
        return findInput(command, 0);
    }

    private long findInput(String originalCommand, long currentNumber) {
        for(long i = 0; i < 8; i++) {
            long a = (currentNumber << 3) + i;
            String output = runCommand(originalCommand, a, 0, 0);
            System.out.println(a + " -> " + output);

            if(originalCommand.equals(output)) return a;
            if(output.length() > originalCommand.length()) return 0;
            if(originalCommand.endsWith(output)) {
                long next = findInput(originalCommand, a);
                if(next != 0) return next;
            }
        }
        return 0;
    }

    private String runCommand(String command, long a, long b, long c) {
        String[] commands = command.split(",");

        List<Long> out = new ArrayList<>();
        for(int i = 0; i < commands.length; i += 2) {
            if(i >= commands.length - 1) break;

            long instruction = Long.parseLong(commands[i]);

            long literalOperand = Long.parseLong(commands[i + 1]);
            long comboOperand = literalOperand;

            if(comboOperand == 4) comboOperand = a;
            else if(comboOperand == 5) comboOperand = b;
            else if(comboOperand == 6) comboOperand = c;

            if(instruction == 0) a = Math.floorDiv(a, (long) Math.pow(2, comboOperand));
            if(instruction == 1) b = b ^ literalOperand;
            if(instruction == 2) b = comboOperand % 8;
            if(instruction == 3) {
                if(a == 0) continue;
                i = (int) (literalOperand - 2);
            }
            if(instruction == 4) b = b^c;
            if(instruction == 5) out.add(comboOperand % 8);
            if(instruction == 6) b = Math.floorDiv(a, (long) Math.pow(2, comboOperand));
            if(instruction == 7) c = Math.floorDiv(a, (long) Math.pow(2, comboOperand));
        }

        return out.toString().replaceAll("[\\[\\] ]", "");
    }

}
