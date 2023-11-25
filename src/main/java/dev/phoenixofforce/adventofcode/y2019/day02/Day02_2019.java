package dev.phoenixofforce.adventofcode.y2019.day02;

import dev.phoenixofforce.adventofcode.common.Puzzle;
import dev.phoenixofforce.adventofcode.common.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Day02_2019 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        return run(input, 12, 2);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        int target = 19690720;
        for(int n = 0; n <= 99; n++) {
            for(int v = 0; v <= 99; v++) {
                if(run(input, n, v) == target) {
                    return 100 * n + v;
                }
            }
        }

        return 0;
    }

    private int run(PuzzleInput input, int noun, int verb) {
        int[] code = Arrays.stream(input.getFile().split(","))
            .mapToInt(Integer::parseInt)
            .toArray();

        code[1] = noun;
        code[2] = verb;

        int index = 0;

        while(code[index] != 99) {
            int op = code[index];
            int pos1 = code[index + 1];
            int pos2 = code[index + 2];
            int target = code[index + 3];

            int out = switch (op) {
                case 1 -> code[pos1] + code[pos2];
                case 2 -> code[pos1] * code[pos2];
                default -> 0;
            };

            code[target] = out;
            index += 4;
        }

        return code[0];
    }

}
