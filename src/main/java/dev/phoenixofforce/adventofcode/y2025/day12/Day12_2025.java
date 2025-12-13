package dev.phoenixofforce.adventofcode.y2025.day12;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Day12_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Integer> blocks = new ArrayList<>();
        for(String p: input.getParagraphs()) {
            if(p.contains("x")) break;
            blocks.add(p.replaceAll("[^#]", "").length());
        }

        int out = 0;
        for(String l: input.getLines()) {
            if(!l.contains("x")) continue;
            System.out.println(l);
            String[] parts = l.split(" ");
            int w = Integer.parseInt(l.split(":")[0].split("x")[0]);
            int h = Integer.parseInt(l.split(":")[0].split("x")[1]);
            int freeBlocks = w * h;

            int usedBlocks = 0;
            for(int i = 1; i < parts.length; i++) {
                int count = Integer.parseInt(parts[i]);
                usedBlocks += count * blocks.get(i - 1);
            }
            if(freeBlocks >= usedBlocks) out++;
        }

        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return "";
    }

}
