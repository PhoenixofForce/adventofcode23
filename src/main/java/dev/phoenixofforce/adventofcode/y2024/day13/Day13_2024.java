package dev.phoenixofforce.adventofcode.y2024.day13;

import dev.phoenixofforce.adventofcode.solver.Position;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day13_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        long out = 0;
        for(String p: input.getParagraphs()) {
            String s = p.split("Button A:")[1];
            String aComponents = s.split("Button B:")[0].trim();
            s = p.split("Button B:")[1];
            String bComponents = s.split("Prize:")[0].trim();
            s = p.split("Prize:")[1];
            String prizeComponents = s.trim();

            Position aIncrement = stringToPosition(aComponents);
            Position bIncrement = stringToPosition(bComponents);
            Position prizePosition = stringToPosition(prizeComponents);

            long requiredTokens = getTokenCount(aIncrement, bIncrement, prizePosition, false);
            System.out.println("a: %s, b: %s, p: %s takes %s tokens".formatted(aIncrement, bIncrement, prizePosition, requiredTokens));
            out += requiredTokens;
        }
        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        long out = 0;
        for(String p: input.getParagraphs()) {
            String s = p.split("Button A:")[1];
            String aComponents = s.split("Button B:")[0].trim();
            s = p.split("Button B:")[1];
            String bComponents = s.split("Prize:")[0].trim();
            s = p.split("Prize:")[1];
            String prizeComponents = s.trim();

            Position aIncrement = stringToPosition(aComponents);
            Position bIncrement = stringToPosition(bComponents);
            Position prizePosition = stringToPosition(prizeComponents);
            prizePosition = prizePosition.offset(10000000000000L, 10000000000000L);

            long requiredTokens = getTokenCount(aIncrement, bIncrement, prizePosition, true);
            System.out.println("a: %s, b: %s, p: %s takes %s tokens".formatted(aIncrement, bIncrement, prizePosition, requiredTokens));
            out += requiredTokens;
        }
        return out;
    }


    private Position stringToPosition(String in) {
        String[] parts = in.replaceAll("[XY,+=]", "").split(" ");
        return new Position(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
    }

    private long getTokenCount(Position aIncrement, Position bIncrement, Position prizePosition, boolean part2) {
        double aMult = aIncrement.getY() - (double) bIncrement.getY() * aIncrement.getX() / bIncrement.getX();
        double aEquals = prizePosition.getY() - (double) (bIncrement.getY() * prizePosition.getX()) / bIncrement.getX();

        double a = aEquals / aMult;
        double b = (double) prizePosition.getX() / bIncrement.getX() - aIncrement.getX() * a / bIncrement.getX();

        if(a > 100 && !part2) return 0;
        if(b > 100 && !part2) return 0;

        List<Long> as = List.of((long) Math.floor(a), (long) Math.ceil(a));
        List<Long> bs = List.of((long) Math.floor(b), (long) Math.ceil(b));

        for(long A: as) {
            for(long B: bs) {
                if(aIncrement.getX() * A + bIncrement.getX() * B != prizePosition.getX()) continue;
                if(aIncrement.getY() * A + bIncrement.getY() * B != prizePosition.getY()) continue;

                return A * 3 + B;
            }
        }

        return 0;
    }

}
