package dev.phoenixofforce.adventofcode.y2025.day04;

import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

@Slf4j
@Component
public class Day04_2025 implements Puzzle {

    private record RemovedState(PuzzleInput newState, int removedRolls) {}

    @Override
    public Object solvePart1(PuzzleInput input) {
        return remove(input).removedRolls;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        PuzzleInput state = input;
        int totalRemovedRolls = 0;
        int dRolls;

        do {
            RemovedState update = remove(state);
            state = update.newState();
            totalRemovedRolls += update.removedRolls();
            dRolls = update.removedRolls();
        } while (dRolls > 0);

        return totalRemovedRolls;
    }

    private RemovedState remove(PuzzleInput input) {
        PuzzleInput copy = input.clone();
        int availableRolls = 0;

        for(int x = 0; x < input.width(); x++) {
            for(int y = 0; y < input.height(); y++) {
                if(input.getChar(x, y) != '@') continue;
                int adjacentCount = 0;
                for(int dx = -1; dx <= 1; dx++) {
                    for(int dy = -1; dy <= 1; dy++) {
                        if(dx == 0 && dy == 0) continue;
                        int nx = x + dx;
                        int ny = y + dy;

                        if(!input.inbounds(nx, ny)) continue;
                        if(input.getChar(nx, ny) == '@') adjacentCount++;
                    }
                }
                if(adjacentCount < 4) {
                    availableRolls++;
                    copy.setChar(x, y, ' ');
                }
            }
        }

        return new RemovedState(copy, availableRolls);
    }

}
