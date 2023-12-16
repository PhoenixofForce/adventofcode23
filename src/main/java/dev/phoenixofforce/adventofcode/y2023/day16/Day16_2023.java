package dev.phoenixofforce.adventofcode.y2023.day16;

import dev.phoenixofforce.adventofcode.common.DirectionUtils;
import dev.phoenixofforce.adventofcode.common.Puzzle;
import dev.phoenixofforce.adventofcode.common.PuzzleInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
public class Day16_2023 implements Puzzle {

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class Pos {
        private int x;
        private int y;
    }

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class Beam {
        private Pos pos;
        private DirectionUtils.Direction4 facing;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        return solve(input, new Pos(0, 0), DirectionUtils.Direction4.EAST);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        long leftRightMax = IntStream.range(0, input.getLines().size())
            .parallel()
            .mapToLong(y -> Math.max(
                solve(input, new Pos(0, y), DirectionUtils.Direction4.EAST),
                solve(input, new Pos(input.getLines().get(y).length() - 1, y), DirectionUtils.Direction4.WEST)
            ))
            .max().orElse(0);

        long topBottomMax = IntStream.range(0, input.getLines().get(0).length())
            .parallel()
            .mapToLong(x -> Math.max(
                solve(input, new Pos(x, 0), DirectionUtils.Direction4.SOUTH),
                solve(input, new Pos(x, input.getLines().size() - 1), DirectionUtils.Direction4.NORTH)
            ))
            .max().orElse(0);

        return Math.max(leftRightMax, topBottomMax);
    }

    private long solve(PuzzleInput input, Pos startPos, DirectionUtils.Direction4 startDir) {
        Set<Beam> energized = new HashSet<>();
        List<Beam> beams = new ArrayList<>();
        beams.add(new Beam(startPos, startDir));

        while(!beams.isEmpty()) {
            for(int b = beams.size() - 1; b >= 0; b--) {
                Beam beam = beams.get(b);
                if(energized.stream().filter(bea -> bea.getPos().equals(beam.getPos()) && bea.getFacing() == beam.getFacing()).count() > 0 ||
                    beam.getPos().x < 0 || beam.getPos().getX() >= input.getLines().get(0).length() ||
                    beam.getPos().y < 0 || beam.getPos().getY() >= input.getLines().size()) {

                    beams.remove(b);
                    continue;
                }
                energized.add(new Beam(
                    new Pos(beam.getPos().getX(), beam.getPos().getY()),
                    beam.getFacing()
                ));

                char c = input.getLines().get(beam.getPos().y).charAt(beam.getPos().x);
                if(c == '|' &&
                    List.of(DirectionUtils.Direction4.EAST, DirectionUtils.Direction4.WEST).contains(beam.facing)
                ) {

                    beam.setFacing(DirectionUtils.Direction4.NORTH);
                    beams.add(0, new Beam(new Pos(beam.getPos().getX(), beam.getPos().getY() + 1), DirectionUtils.Direction4.SOUTH));
                    b++;
                }
                else if(c == '-' &&
                    List.of(DirectionUtils.Direction4.NORTH, DirectionUtils.Direction4.SOUTH).contains(beam.facing)
                ) {

                    beam.setFacing(DirectionUtils.Direction4.EAST);
                    beams.add(0, new Beam(new Pos(beam.getPos().getX() - 1, beam.getPos().getY()), DirectionUtils.Direction4.WEST));
                    b++;
                } else if(c == '/') {
                    beam.setFacing(switch (beam.getFacing()) {
                        case EAST -> DirectionUtils.Direction4.NORTH;
                        case SOUTH -> DirectionUtils.Direction4.WEST;
                        case WEST -> DirectionUtils.Direction4.SOUTH;
                        case NORTH -> DirectionUtils.Direction4.EAST;
                    });
                } else if(c == '\\') {
                    beam.setFacing(switch (beam.getFacing()) {
                        case EAST -> DirectionUtils.Direction4.SOUTH;
                        case SOUTH -> DirectionUtils.Direction4.EAST;
                        case WEST -> DirectionUtils.Direction4.NORTH;
                        case NORTH -> DirectionUtils.Direction4.WEST;
                    });
                }

                DirectionUtils.Direction4 face = beam.getFacing();
                Pos newPos = new Pos(
                    beam.getPos().getX() + face.toArray()[0],
                    beam.getPos().getY() + face.toArray()[1]
                );
                beam.setPos(newPos);
            }
        }

        return energized.stream()
            .map(Beam::getPos)
            .distinct()
            .count();
    }

}
