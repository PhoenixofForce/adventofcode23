package dev.phoenixofforce.adventofcode.y2023.day16;

import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class Day16_2023 implements Puzzle {

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class Beam {
        private Position pos;
        private Direction facing;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        return solve(input, new Position(0, 0), Direction.EAST);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        long leftRightMax = IntStream.range(0, input.getLines().size())
            .parallel()
            .mapToLong(y -> Math.max(
                solve(input, new Position(0, y), Direction.EAST),
                solve(input, new Position(input.getLines().get(y).length() - 1, y), Direction.WEST)
            ))
            .max().orElse(0);

        long topBottomMax = IntStream.range(0, input.getLines().get(0).length())
            .parallel()
            .mapToLong(x -> Math.max(
                solve(input, new Position(x, 0), Direction.SOUTH),
                solve(input, new Position(x, input.getLines().size() - 1), Direction.NORTH)
            ))
            .max().orElse(0);

        return Math.max(leftRightMax, topBottomMax);
    }

    private long solve(PuzzleInput input, Position startPos, Direction startDir) {
        Set<Beam> energized = new HashSet<>();
        List<Beam> beams = new ArrayList<>();
        beams.add(new Beam(startPos, startDir));

        while(!beams.isEmpty()) {
            for(int b = beams.size() - 1; b >= 0; b--) {
                Beam beam = beams.get(b);
                if(energized.stream().filter(bea -> bea.getPos().equals(beam.getPos()) && bea.getFacing() == beam.getFacing()).count() > 0 ||
                    beam.getPos().getX() < 0 || beam.getPos().getX() >= input.getLines().get(0).length() ||
                    beam.getPos().getY() < 0 || beam.getPos().getY() >= input.getLines().size()) {

                    beams.remove(b);
                    continue;
                }
                energized.add(new Beam(
                    new Position(beam.getPos().getX(), beam.getPos().getY()),
                    beam.getFacing()
                ));

                char c = input.getLines().get((int) beam.getPos().getY()).charAt((int) beam.getPos().getX());
                if(c == '|' &&
                    List.of(Direction.EAST, Direction.WEST).contains(beam.facing)
                ) {

                    beam.setFacing(Direction.NORTH);
                    beams.add(0, new Beam(beam.getPos().offset(0, 1), Direction.SOUTH));
                    b++;
                }
                else if(c == '-' &&
                    List.of(Direction.NORTH, Direction.SOUTH).contains(beam.facing)
                ) {

                    beam.setFacing(Direction.EAST);
                    beams.add(0, new Beam(beam.getPos().offset(-1, 0), Direction.WEST));
                    b++;
                } else if(c == '/') {
                    beam.setFacing(switch (beam.getFacing()) {
                        case EAST -> Direction.NORTH;
                        case SOUTH -> Direction.WEST;
                        case WEST -> Direction.SOUTH;
                        case NORTH -> Direction.EAST;
                    });
                } else if(c == '\\') {
                    beam.setFacing(switch (beam.getFacing()) {
                        case EAST -> Direction.SOUTH;
                        case SOUTH -> Direction.EAST;
                        case WEST -> Direction.NORTH;
                        case NORTH -> Direction.WEST;
                    });
                }

                Direction face = beam.getFacing();
                Position newPos = beam.getPos().applyDirection(face);
                beam.setPos(newPos);
            }
        }

        return energized.stream()
            .map(Beam::getPos)
            .distinct()
            .count();
    }

}
