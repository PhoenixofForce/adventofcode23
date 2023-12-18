package dev.phoenixofforce.adventofcode.y2021.day03;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Day03_2021 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        int[] numberOfOnes = getOneCount(input.getLines());

        int gamma = 0;
        int eps = 0;
        for(int i = 0; i < numberOfOnes.length; i++) {
            gamma <<= 1;
            eps <<= 1;

            if(numberOfOnes[i] > input.getLines().size()/2.0) {
                gamma |= 1;
            } else {
                eps |= 1;
            }
        }

        return gamma * eps;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<String> lineCOO = new ArrayList<>(input.getLines());
        List<String> lineOO = new ArrayList<>(input.getLines());

        int index = 0;
        while(lineCOO.size() > 1) {
            int inCopy = index;
            int[] numberOnes = getOneCount(lineCOO);
            int number = numberOnes[index] >= lineCOO.size() / 2.0? 1: 0;
            lineCOO = lineCOO.stream().filter(s -> s.charAt(inCopy) != (number + "").charAt(0)).collect(Collectors.toList());
            index++;
        }

        index = 0;
        while(lineOO.size() > 1) {
            int inCopy = index;
            int[] numberOnes = getOneCount(lineOO);
            int number = numberOnes[index] >= lineOO.size() / 2.0? 1: 0;
            lineOO = lineOO.stream().filter(s -> s.charAt(inCopy) == (number + "").charAt(0)).collect(Collectors.toList());
            index++;
        }

        int coo = Integer.parseInt(lineCOO.get(0), 2);
        int oo = Integer.parseInt(lineOO.get(0), 2);

        return coo * oo;
    }

    public static int[] getOneCount(List<String> lines ) {
        int[] numberOfOnes = new int[lines.get(0).length()];

        for(String line: lines) {
            for(int i = 0; i < line.length(); i++) {
                if(line.charAt(i) == '1') numberOfOnes[i] = numberOfOnes[i] + 1;
            }
        }

        return numberOfOnes;
    }

}
