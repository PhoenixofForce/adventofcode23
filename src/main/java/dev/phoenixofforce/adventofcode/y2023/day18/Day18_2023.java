package dev.phoenixofforce.adventofcode.y2023.day18;

import dev.phoenixofforce.adventofcode.common.DirectionUtils;
import dev.phoenixofforce.adventofcode.common.Puzzle;
import dev.phoenixofforce.adventofcode.common.PuzzleInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class Day18_2023 implements Puzzle {

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class Pos {
        private long x;
        private long y;
    }

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class Edge {
        private Pos from;
        private Pos to;

        private boolean contains(Pos p) {
            return getMinY() <= p.getY() && p.getY() <= getMaxY() &&
                    getMinX() <= p.getX() && p.getX() <= getMaxX();
        }

        private long getMinY() {
            return Math.min(from.y, to.y);
        }

        private long getMaxY() {
            return Math.max(from.y, to.y);
        }

        private long getMinX() {
            return Math.min(from.x, to.x);
        }

        private long getMaxX() {
            return Math.max(from.x, to.x);
        }

        private long getMinX(long y) {
            if(getMinY() <= y && y <= getMaxY()) return getMinX();
            return Long.MAX_VALUE;
        }

        private long getMaxX(long y) {
            if(getMinY() <= y && y <= getMaxY()) return getMaxX();
            return Long.MIN_VALUE;
        }

        private long length() {
            return (getMaxX() - getMinX()) + (getMaxY() - getMinY());
        }
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        Set<Edge> coloredTunnels = dig(input.getLines());
        long minY = coloredTunnels.stream().mapToLong(Edge::getMinY).min().getAsLong();
        long maxY = coloredTunnels.stream().mapToLong(Edge::getMaxY).max().getAsLong();

        long out = 0;
        for(long y = minY - 1; y <= maxY + 1; y++) {
            boolean isInside = false;
            long finalY = y;
            long minX = coloredTunnels.stream().mapToLong(e -> e.getMinX(finalY)).min().getAsLong();
            long maxX = coloredTunnels.stream().mapToLong(e -> e.getMaxX(finalY)).max().getAsLong();

            for(long x = minX - 1; x <= maxX + 1; x++) {
                Pos current = new Pos(x, y);

                boolean containsCurrent = coloredTunnels.stream().anyMatch( e-> e.contains(current));
                boolean containsAbove = coloredTunnels.stream().anyMatch( e-> e.contains(new Pos(current.getX(), current.getY() - 1)));
                boolean isPipe =  containsCurrent &&
                    containsAbove &&
                    coloredTunnels.stream().anyMatch( e-> e.contains(new Pos(current.getX(), current.getY() + 1)));

                boolean isL = containsCurrent &&
                    containsAbove &&
                    coloredTunnels.stream().anyMatch( e-> e.contains(new Pos(current.getX() + 1, current.getY())));

                boolean isJ = containsCurrent &&
                    containsAbove &&
                    coloredTunnels.stream().anyMatch( e-> e.contains(new Pos(current.getX() - 1, current.getY())));

                if(isPipe || isL || isJ) isInside = !isInside;

                if(isInside || containsCurrent) out++;
            }
        }

        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return solvePart1(input.mapLines(
            s -> {
                String color = s.split(" ")[2].replaceAll("[(#)]", "");
                String direction = switch (color.charAt(color.length() - 1)) {
                    case '0' -> "R";
                    case '1' -> "D";
                    case '2' -> "L";
                    case '3' -> "U";
                    default -> "i";
                };
                color = color.substring(0, color.length() - 1);
                long length = Long.parseLong(color, 16);
                return direction + " " + length + " (#000000)";
            }
        ));
    }

    private Set<Edge> dig(List<String> lines) {
        Set<Edge> out = new HashSet<>();

        Pos current = new Pos(0, 0);
        for(String s: lines) {
            DirectionUtils.Direction4 direction = DirectionUtils.getDirection4(s.split(" ")[0]);
            long length = Long.parseLong(s.split(" ")[1]);

            Pos other = new Pos(
                current.getX() + direction.toArray()[0] * (length),
                current.getY() + direction.toArray()[1] * (length)
            );
            out.add(new Edge(current, other));
            current = other;
        }

        return out;
    }

}
