package dev.phoenixofforce.adventofcode.y2024.day04;

import dev.phoenixofforce.adventofcode.solver.Position;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day04_2024 implements Puzzle {

    private static final List<int[]> EIGHT_DIRECTIONS = List.of(
        new int[]{-1, -1},
        new int[]{-1, 0},
        new int[]{-1, 1},
        new int[]{0, -1},
        new int[]{0, 1},
        new int[]{1, -1},
        new int[]{1, 0},
        new int[]{1, 1}
    );

    @Override
    public Object solvePart1(PuzzleInput input) {
        long xmasses = 0;

        for(int y = 0; y < input.getLines().size(); y++) {
            String line = input.getLines().get(y);
            for(int x = 0;  x < line.length(); x++) {

                Position xPos = new Position(x, y);
                char currentChar = input.getChar(xPos);
                if(currentChar != 'X') continue;

                for(int[] dir: EIGHT_DIRECTIONS) {
                    Position mPos = xPos.applyDirection(dir, 1);
                    Position aPos = xPos.applyDirection(dir, 2);
                    Position sPos = xPos.applyDirection(dir, 3);

                    if(input.getCharOrElseDot(mPos) != 'M') continue;
                    if(input.getCharOrElseDot(aPos) != 'A') continue;
                    if(input.getCharOrElseDot(sPos) != 'S') continue;

                    xmasses++;
                }
            }
        }

        return xmasses;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        long xmasses = 0;

        for(int y = 0; y < input.getLines().size(); y++) {
            String line = input.getLines().get(y);
            for(int x = 0;  x < line.length(); x++) {

                Position aPos = new Position(x, y);
                char currentChar = input.getChar(aPos);
                if(currentChar != 'A') continue;

                Position topLeft = aPos.applyDirection(new int[]{-1, -1}, 1);
                Position bottomRight = aPos.applyDirection(new int[]{1, 1}, 1);
                Position topRight = aPos.applyDirection(new int[]{1, -1}, 1);
                Position bottomLeft = aPos.applyDirection(new int[]{-1, 1}, 1);

                if ((input.getCharOrElseDot(topLeft) == 'M' && input.getCharOrElseDot(bottomRight) == 'S' ||
                        input.getCharOrElseDot(topLeft) == 'S' && input.getCharOrElseDot(bottomRight) == 'M') &&
                    (input.getCharOrElseDot(topRight) == 'M' && input.getCharOrElseDot(bottomLeft) == 'S' ||
                        input.getCharOrElseDot(topRight) == 'S' && input.getCharOrElseDot(bottomLeft) == 'M')

                ) {
                    xmasses++;
                }
            }
        }

        return xmasses;
    }

}
