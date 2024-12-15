package dev.phoenixofforce.adventofcode.y2024.day15;

import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day15_2024 implements Puzzle {

    @Data @AllArgsConstructor
    private static class WideBox {
        private Position pos1;
        private Position pos2;

        private boolean anyMatches(Position pos) {
            return pos.equals(pos1) || pos.equals(pos2);
        }
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        return solve(input, false);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return solve(input, true);
    }

    private long solve(PuzzleInput input, boolean part2) {
        Position robot = null;
        Map<Position, Boolean> isWall = new HashMap<>();
        List<WideBox> boxes = new ArrayList<>();

        int y = 0;
        for(; y < input.height(); y++) {
            if(input.getLines().get(y).isBlank()) {
                y++;
                break;
            }
            for(int x = 0; x < input.width(); x++) {
                char c = input.getChar(x, y);

                if(part2) {
                    if(c == '@') robot = new Position(x * 2L, y);
                    if (c == 'O') {
                        boxes.add(new WideBox(new Position(x * 2L, y), new Position(x * 2L + 1, y)));
                    }
                    isWall.put(new Position(x * 2L, y), c == '#');
                    isWall.put(new Position(x * 2L + 1, y), c == '#');
                    continue;
                }

                if(c == '@') robot = new Position(x, y);
                if (c == 'O') boxes.add(new WideBox(new Position(x, y), new Position(x, y)));
                isWall.put(new Position(x, y), c == '#');
            }
        }

        String moves = input.getLines().stream()
            .skip(y)
            .reduce("", (a, b) -> a + b);

        for(char c: moves.toCharArray()) {
            if(robot == null) return 0;

            Direction direction = Direction.getDirection4(c);
            if(!canMove(robot, direction, boxes, isWall)) continue;
            robot = move(robot, direction, boxes, isWall);
        }

        return boxes.stream()
            .mapToLong(e -> e.getPos1().getY() * 100 + e.getPos1().getX())
            .sum();
    }


    private boolean canMove(Position position, Direction direction, List<WideBox> boxes, Map<Position, Boolean> isWall) {
        Position nextPosition = position.applyDirection(direction);
        if(isWall.get(nextPosition)) return false;

        Optional<WideBox> box = boxes.stream().filter(e -> e.anyMatches(nextPosition)).findFirst();
        if(box.isPresent()) {
            boolean canFirstMove = direction == Direction.EAST ||
                canMove(box.get().getPos1(), direction, boxes, isWall);

            boolean canSecondMove = direction == Direction.WEST ||
                canMove(box.get().getPos2(), direction, boxes, isWall);

            return canFirstMove && canSecondMove;
        }
        return true;
    }

    private Position move(Position position, Direction direction, List<WideBox> boxes, Map<Position, Boolean> isWall) {
        Position nextPosition = position.applyDirection(direction);
        if(isWall.get(nextPosition)) return null;

        Optional<WideBox> box = boxes.stream().filter(e -> e.anyMatches(nextPosition)).findFirst();
        box.ifPresent(wideBox -> moveBox(wideBox, direction, boxes, isWall));
        return nextPosition;
    }

    private void moveBox(WideBox box, Direction direction, List<WideBox> boxes, Map<Position, Boolean> isWall) {
        Position nextPosition1 = box.getPos1().applyDirection(direction);
        Position nextPosition2 = box.getPos2().applyDirection(direction);

        if(isWall.get(nextPosition1)) return;
        if(isWall.get(nextPosition2)) return;

        Optional<WideBox> nextBox = boxes.stream().filter(e -> e.anyMatches(nextPosition1) && e != box).findFirst();
        nextBox.ifPresent(wideBox -> moveBox(wideBox, direction, boxes, isWall));

        nextBox = boxes.stream().filter(e -> e.anyMatches(nextPosition2) && e != box).findFirst();
        nextBox.ifPresent(wideBox -> moveBox(wideBox, direction, boxes, isWall));

        box.setPos1(nextPosition1);
        box.setPos2(nextPosition2);
    }

}
