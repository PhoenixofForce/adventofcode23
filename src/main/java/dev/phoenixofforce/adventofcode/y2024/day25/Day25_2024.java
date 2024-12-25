package dev.phoenixofforce.adventofcode.y2024.day25;

import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day25_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<int[]> keys = new ArrayList<>();
        List<int[]> molds = new ArrayList<>();

        long maxKeyLength = 0;

        for(List<String> entry: input.getParagraphsAsList()) {
            maxKeyLength = entry.size() - 2;

            boolean isMold = entry.getFirst().charAt(0) == '#';
            int[] cutout = new int[entry.getFirst().length()];
            for(String line: entry) {
                for(int i = 0; i < line.length(); i++) {
                    if(line.charAt(i) == '.') continue;
                    cutout[i] = cutout[i] + 1;
                }
            }
            for(int i = 0; i < cutout.length; i++) cutout[i] = cutout[i] - 1;

            if(isMold) molds.add(cutout);
            else keys.add(cutout);
        }

        long out = 0;
        for(int[] key: keys) {
            for(int[] mold: molds) {
                boolean fit = true;
                for(int i = 0; i < key.length; i++) {
                    if(key[i] + mold[i] > maxKeyLength) {
                        fit = false;
                    }
                }
                if(fit) out++;
            }
        }

        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return "";
    }

}
