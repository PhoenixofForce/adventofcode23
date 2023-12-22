package dev.phoenixofforce.adventofcode.y2023.day22;

import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Day22_2023 implements Puzzle {

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class Brick {
        private Position first, second;

        public Brick(String line) {
            List<Position> positions = Arrays.stream(line.split("~"))
                .map(p -> {
                    List<Integer> coords = Arrays.stream(p.split(","))
                        .map(Integer::parseInt)
                        .toList();
                    return new Position(coords.get(0), coords.get(1), coords.get(2));
                }).toList();

            this.first = positions.getFirst();
            this.second = positions.getLast();
        }

        @Override
        public Brick clone() {
            return new Brick(first.clone(), second.clone());
        }

        boolean overlaps(Brick other) {
            return other.getFirst().getX() <= second.getX() && first.getX() <= other.getSecond().getX() &&
                other.getFirst().getY() <= second.getY() && first.getY() <= other.getSecond().getY() &&
                other.getFirst().getZ() <= second.getZ() && first.getZ() <= other.getSecond().getZ();
        }
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Brick> bricks = input.getLines().stream()
            .map(Brick::new)
            .sorted(Comparator.comparing(b -> b.getFirst().getZ()))
            .toList();

        simulateFalling(bricks);

        long out = 0;
        for(Brick brick: bricks) {
            Brick brickButHigher = brick.clone();
            brickButHigher.setSecond(brickButHigher.getSecond().offset(0, 0, 1));
            List<Brick> supports = bricks.stream().filter(e -> e != brick && e.overlaps(brickButHigher)).toList();

            boolean anyHasBrickAsOnlySupport = false;
            for(Brick supported: supports) {
                Brick supportedButMoreDown = supported.clone();
                supportedButMoreDown.setFirst(supportedButMoreDown.getFirst().offset(0, 0, -1));

                List<Brick> supportedBy = bricks.stream().filter(e -> e != supported && e.overlaps(supportedButMoreDown)).toList();
                if(supportedBy.size() == 1) {
                    anyHasBrickAsOnlySupport = true;
                    break;
                }
            }
            if(!anyHasBrickAsOnlySupport) {
                out++;
            }
        }

        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<Brick> bricks = input.getLines().stream()
            .map(Brick::new)
            .sorted(Comparator.comparing(b -> b.getFirst().getZ()))
            .toList();

        simulateFalling(bricks);

        long out = 0;
        for(Brick brick: bricks) {
            List<Brick> simulation = new ArrayList<>(bricks.subList(0, bricks.size()).stream()
                .map(Brick::clone).toList());
            simulation.remove(brick);

            simulateFalling(simulation);

            long fallen = 0;

            int offset = 0;
            for(int i = 0; i < bricks.size(); i++) {
                Brick b = bricks.get(i);
                if(b.equals(brick)) {
                    offset = 1;
                    continue;
                }

                if(!b.equals(simulation.get(i - offset))) fallen += 1;
            }

            out += fallen;
        }

        return out;
    }

    private void simulateFalling(List<Brick> bricks) {
        boolean changes = true;
        while(changes) {
            changes = false;
            for(int i = 0; i < bricks.size(); i++) {
                Brick brick = bricks.get(i);
                if(brick.first.getZ() == 1) continue;

                brick.setFirst(brick.getFirst().offset(0, 0, -1));
                brick.setSecond(brick.getSecond().offset(0, 0, -1));

                if(bricks.stream().anyMatch(b -> b != brick && b.overlaps(brick))) {
                    brick.setFirst(brick.getFirst().offset(0, 0, 1));
                    brick.setSecond(brick.getSecond().offset(0, 0, 1));
                } else {
                    changes = true;
                }
            }
        }
    }

}
