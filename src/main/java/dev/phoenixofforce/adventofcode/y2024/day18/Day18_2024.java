package dev.phoenixofforce.adventofcode.y2024.day18;

import dev.phoenixofforce.adventofcode.solver.Dijkstra;
import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Day18_2024 implements Puzzle {

    private static final int MAX_POS = 70;
    private static final int SIMULATION_SIZE = 1024;


    @Override
    public Object solvePart1(PuzzleInput input) {
        Position start = new Position(0, 0);
        Position end = new Position(MAX_POS, MAX_POS);
        Set<Position> walls = getWalls(input);

        Dijkstra.End<Position> path = Dijkstra.from(start)
            .to(end)
            .generateNextSteps(state -> getNextSteps(walls, state))
            .getFirst();

        for(int y = 0; y <= end.getY(); y++) {
            for(int x = 0; x <= end.getX(); x++) {
                Position p = new Position(x, y);
                char c = '.';
                if(walls.contains(p)) c = '#';
                if(path.getPath().stream().anyMatch(e -> e.equals(p))) c = 'O';
                System.out.print(c);
            }
            System.out.println();
        }

        return path.getDistance();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        Position start = new Position(0, 0);
        Position end = new Position(MAX_POS, MAX_POS);

        for(int i = SIMULATION_SIZE + 1; i < input.getLines().size(); i++) {
            Set<Position> walls = getWalls(input, i);

            Dijkstra.End<Position> path = Dijkstra.from(start)
                .to(end)
                .generateNextSteps(state -> getNextSteps(walls, state))
                .getFirst();

            if(path.isEmpty()) {
                return input.getLines().get(i - 1);
            }
        }
        return 0;
    }

    private List<Position> getNextSteps(Set<Position> walls, Position state) {
        List<Position> out = new ArrayList<>();
        for(Position p: Direction.getNeighbors4(Position::new, (int) state.getX(), (int) state.getY())) {
            if(p.getX() < 0 || p.getX() > MAX_POS) continue;
            if(p.getY() < 0 || p.getY() > MAX_POS) continue;
            if(walls.contains(p)) continue;
            out.add(p);
        }
        return out;
    }

    private Set<Position> getWalls(PuzzleInput input) {
        return getWalls(input, SIMULATION_SIZE);
    }

    private Set<Position> getWalls(PuzzleInput input, int limit) {
        return input.getLines().stream()
            .limit(limit)
            .map(e -> new Position(Long.parseLong(e.split(",")[0]), Long.parseLong(e.split(",")[1])))
            .collect(Collectors.toSet());
    }

}
