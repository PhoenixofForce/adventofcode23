package dev.phoenixofforce.adventofcode.y2021.day23;

import dev.phoenixofforce.adventofcode.solver.Dijkstra;
import dev.phoenixofforce.adventofcode.solver.ArrayUtils;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Day23_2021 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        return solve(input.getLines(), false);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return solve(input.getLines(), true);
    }

    private long solve(List<String> lines, boolean part2) {
        if(part2) {
            lines.add(3, "  #D#B#A#C#");
            lines.add(3, "  #D#C#B#A#");
        }

        char[][] map = new char[lines.size()][lines.get(0).length()];
        for(int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);

            for(int j = 0; j < lines.get(0).length(); j++) {
                if(j >= s.length()) map[i][j] = ' ';
                else map[i][j] = lines.get(i).charAt(j);
            }
        }

        System.out.println(ArrayUtils.toString(map));

        State start = new State(map, 0);
        return Dijkstra.findPathWithMultipleEndsAndHeuristic(start, State::isEnd, State::getStates, (a, b) -> a.getScore(), State::getHeuristic);
    }

}
