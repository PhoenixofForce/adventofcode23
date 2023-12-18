package dev.phoenixofforce.adventofcode.y2023.day15;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class Day15_2023 implements Puzzle {

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class Lense {
        @EqualsAndHashCode.Exclude
        private String focalLength;
        private String label;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        return Arrays.stream(input.getFile().split(","))
            .map(String::trim)
            .mapToLong(this::hash)
            .sum();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<List<Lense>> boxes = new ArrayList<>();
        for(int i = 0; i < 256; i++) boxes.add(new ArrayList<>());

        for(String s: input.getFile().split(",")) {
            String label = s.replaceAll("[^A-z]", "");
            String operation = s.replaceAll("[A-z0-9]", "");
            String focalLength = s.replaceAll("[^0-9]", "");

            int box = hash(label);

            Lense lense = new Lense(focalLength, label);
            List<Lense> relevantBox = boxes.get(box);

            if("-".equals(operation)) {
                relevantBox.removeIf(l -> l.getLabel().equals(label));
            } else if("=".equals(operation)) {
                if(relevantBox.contains(lense)) {
                    relevantBox.stream()
                        .filter(l -> l.getLabel().equals(label))
                        .findFirst().get().setFocalLength(focalLength);
                } else {
                    relevantBox.add(lense);
                }
            }
            System.out.println("After " + s);
            System.out.println(box + ": " + relevantBox.stream().map(l -> l.getLabel() + " " + l.getFocalLength()).toList());

        }

        long out = 0;
        for(int i = 0; i < boxes.size(); i++) {
            long thisPower = (i + 1);
            for(int j = 0; j < boxes.get(i).size(); j++) {
                out = out + thisPower * (j+1) * Long.parseLong(boxes.get(i).get(j).getFocalLength());
            }
        }

        return out;
    }

    private int hash(String in) {
        int out = 0;
        for(char c: in.toCharArray()) {
            out += c;
            out *= 17;
            out %= 256;
        }
        return out;
    }

}
