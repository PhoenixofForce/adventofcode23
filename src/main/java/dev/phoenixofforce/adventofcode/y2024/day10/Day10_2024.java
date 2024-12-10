package dev.phoenixofforce.adventofcode.y2024.day10;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Day10_2024 implements Puzzle {

    long part2solution = 0;

    @Override
    public Object solvePart1(PuzzleInput input) {
        long out = 0;
        for(int x = 0; x < input.width(); x++) {
            for(int y = 0;  y < input.height(); y++) {
                char c = input.getChar(x, y);
                if(c != '0') continue;

                List<List<Position>> allPaths = findAllPaths(input, new Position(x, y));
                out += allPaths.stream().map(List::getLast).distinct().count();
                part2solution += allPaths.size();
            }
        }

        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return part2solution;
    }

    private List<List<Position>> findAllPaths(PuzzleInput input, Position start) {
        List<List<Position>> allPaths = new ArrayList<>();
        List<List<Position>> openPaths = new ArrayList<>();

        openPaths.add(List.of(start));

        while(!openPaths.isEmpty()) {
            List<Position> path = openPaths.removeFirst();
            for(Position next: getNextSteps(input, path.getLast())) {
                List<Position> newPath = new ArrayList<>(path);
                newPath.add(next);

                if(input.getChar(next) == '9') allPaths.add(newPath);
                else openPaths.add(newPath);
            }
        }

        return allPaths;
    }

    private List<Position> getNextSteps(PuzzleInput input, Position current) {
        List<Position> next = new ArrayList<>();
        int currentHeight = Integer.parseInt(input.getChar(current) + "");
        for(Position p: Direction.getNeighbors4(Position::new, (int) current.getX(), (int) current.getY())) {
            if(!input.inbounds(p)) continue;
            int nextHeight = Integer.parseInt(input.getChar(p) + "");
            if(nextHeight != currentHeight + 1) continue;
            next.add(p);
        }
        return next;
    }
}
