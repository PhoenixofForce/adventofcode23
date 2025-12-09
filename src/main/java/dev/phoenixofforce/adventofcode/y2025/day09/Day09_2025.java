package dev.phoenixofforce.adventofcode.y2025.day09;

import dev.phoenixofforce.adventofcode.meta.ProgressLogger;
import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Day09_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Position> tiles = input.getLines()
                .stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Position(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
                })
                .toList();

        long largestRect = 0;
        for(int i = 1; i < tiles.size(); i++) {
            for(int j = 0; j < i; j++) {
                Position p1 = tiles.get(i);
                Position p2 = tiles.get(j);

                long rect = (Math.max(p1.getX(), p2.getX()) - Math.min(p1.getX(), p2.getX())  + 1) * (Math.max(p1.getY(), p2.getY()) - Math.min(p1.getY(), p2.getY()) + 1);
                log.info("{} and {} = {}", p1, p2, rect);
                if(rect > largestRect) largestRect = rect;
            }
        }

        return largestRect;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<Position> redTiles = input.getLines()
                .stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Position(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
                })
                .toList();

        Set<Position> greenTiles = new HashSet<>();
        for(int i = 0; i < redTiles.size(); i++) {
            Position current = redTiles.get(i);
            Position next = redTiles.get((i + 1) % redTiles.size());

            for(long x = Math.min(current.getX(), next.getX()); x <= Math.max(current.getX(), next.getX()); x++) {
                for(long y = Math.min(current.getY(), next.getY()); y <= Math.max(current.getY(), next.getY()); y++) {
                    greenTiles.add(new Position(x, y));
                }
            }
        }

        int count = 0;
        long max = (long) redTiles.size() * (redTiles.size() - 1) / 2;

        long largestRect = 0;
        for(int i = 1; i < redTiles.size(); i++) {
            for(int j = 0; j < i; j++) {
                Position p1 = redTiles.get(i);
                Position p2 = redTiles.get(j);

                long rect = (Math.max(p1.getX(), p2.getX()) - Math.min(p1.getX(), p2.getX())  + 1) * (Math.max(p1.getY(), p2.getY()) - Math.min(p1.getY(), p2.getY()) + 1);
                if(rect > largestRect && inbounds(redTiles, greenTiles, p1, p2)) largestRect = rect;

                count++;
                ProgressLogger.log(max, count, largestRect);
            }
        }

        return largestRect;
    }

    private boolean inbounds(List<Position> redTiles, Set<Position> greenTiles, Position current, Position next) {
        for(long x = Math.min(current.getX(), next.getX()); x <= Math.max(current.getX(), next.getX()); x++) {
            Position p1 = new Position(x, current.getY());
            Position p2 = new Position(x, next.getY());
            if(!inbounds(redTiles, greenTiles, p1)) return false;
            if(!inbounds(redTiles, greenTiles, p2)) return false;
        }
        for(long y = Math.min(current.getY(), next.getY()); y <= Math.max(current.getY(), next.getY()); y++) {
            Position p1 = new Position(current.getX(), y);
            Position p2 = new Position(next.getX(), y);
            if(!inbounds(redTiles, greenTiles, p1)) return false;
            if(!inbounds(redTiles, greenTiles, p2)) return false;
        }
        return true;
    }

    private Map<Position, Boolean> cache = new HashMap<>();
    private boolean inbounds(List<Position> redTiles, Set<Position> greenTiles, Position p) {
        if(greenTiles.contains(p)) return true;
        if(cache.containsKey(p)) return cache.get(p);

        boolean inside = false;
        for(int i = 0; i < redTiles.size(); i++) {
            Position current = redTiles.get(i);
            Position next = redTiles.get((i + 1) % redTiles.size());

            if ((current.getY() > p.getY()) != (next.getY() > p.getY())) {
                double x = current.getX() + (double) ((p.getY() - current.getY()) * (next.getX() - current.getX())) / (next.getY() - current.getY());
                if (x > p.getX()) inside = !inside;
            }
        }

        cache.put(p, inside);
        return inside;
    }

}
