package dev.phoenixofforce.adventofcode.y2024.day20;

import dev.phoenixofforce.adventofcode.solver.Dijkstra;
import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day20_2024 implements Puzzle {

    @Data @AllArgsConstructor
    private static class Cheat {
        private Position start;
        private Position end;
        private long saved;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        return getCheatsSavingMoreThan100Seconds(input, 2);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return getCheatsSavingMoreThan100Seconds(input, 20);
    }

    private long getCheatsSavingMoreThan100Seconds(PuzzleInput input, long cheatSeconds) {
        Position start = null;
        Position end = null;
        Map<Position, Boolean> isWall = new HashMap<>();

        for(int x = 0; x < input.width(); x++) {
            for(int y = 0; y < input.height(); y++) {
                Position position = new Position(x, y);
                char c = input.getChar(x, y);

                if(c == 'S') start = position;
                else if(c == 'E') end = position;

                isWall.put(position, c == '#');
            }
        }

        Dijkstra.End<Position> path = Dijkstra.from(start)
            .to(end)
            .generateNextSteps(state -> getNextSteps(isWall, state))
            .getFirst();

        long normalSeconds = path.getDistance();

        Map<Position, Long> normalDistances = new HashMap<>();
        for(int i = 0; i < path.getPath().size(); i++) {
            Position element = path.getPath().get(i);
            long distance = normalSeconds - i;
            normalDistances.put(element, distance);
        }

        Set<Cheat> cheats = new HashSet<>();
        for(Position position: path.getPath()) {
            List<Cheat> cheatSkips = new ArrayList<>();
            for(long dx = -cheatSeconds; dx <= cheatSeconds; dx++) {
                for(long dy = -cheatSeconds; dy <= cheatSeconds; dy++) {
                    long steps = Math.abs(dx) + Math.abs(dy);
                    if(steps > cheatSeconds) continue;
                    cheatSkips.add(new Cheat(null, new Position(position.getX() + dx, position.getY() + dy), steps));
                }
            }

            long normalDistance = normalDistances.get(position);
            for(Cheat cheat: cheatSkips) {
                if(isWall.getOrDefault(cheat.getEnd(), true)) continue;
                long distanceAfterCheat = normalDistances.get(cheat.getEnd());
                long savedDistance = normalDistance - distanceAfterCheat - cheat.getSaved();
                if(savedDistance >= 100) {
                    cheats.add(new Cheat(position, cheat.getEnd(), savedDistance));
                }
            }
        }

        return cheats.size();
    }

    private List<Position> getNextSteps(Map<Position, Boolean> isWall, Position currentState) {
        List<Position> out = new ArrayList<>();

        for(Position p: Direction.getNeighbors4(Position::new, (int) currentState.getX(), (int) currentState.getY())) {
            if(!isWall.getOrDefault(p, true)) {
                out.add(p);
            }
        }

        return out;
    }
}
