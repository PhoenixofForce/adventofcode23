package dev.phoenixofforce.adventofcode.y2021.day02;

import dev.phoenixofforce.adventofcode.common.Puzzle;
import dev.phoenixofforce.adventofcode.common.PuzzleInput;
import org.springframework.stereotype.Component;

@Component
public class Day02_2021 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        int x = 0;
        int z = 0;

        for(String[] line: input.getLines().stream().map(l -> l.split(" ")).toList()) {
            String dir = line[0];
            int length = Integer.parseInt(line[1]);

            if(dir.equalsIgnoreCase("down")) z += length;
            else if(dir.equalsIgnoreCase("up")) z -= length;
            else x+=length;
        }

        return x * z;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        int x = 0;
        int z = 0;
        int aim = 0;

        for(String[] line: input.getLines().stream().map(l -> l.split(" ")).toList()) {
            String dir = line[0];
            int length = Integer.parseInt(line[1]);

            if(dir.equalsIgnoreCase("down")) aim += length;
            else if(dir.equalsIgnoreCase("up")) aim -= length;
            else {
                x += length;
                z += aim * length;
            }
        }

        return x * z;
    }

}
