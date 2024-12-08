package dev.phoenixofforce.adventofcode.y2024.day08;

import dev.phoenixofforce.adventofcode.solver.Position;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day08_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        return solve(input, false);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return solve(input, true);
    }

    private long solve(PuzzleInput input, boolean part2) {
        List<Position> allAntennas = new ArrayList<>();
        Map<Position, Character> frequencies = new HashMap<>();
        Set<Position> antinodes = new HashSet<>();

        for(int x = 0; x < input.width(); x++) {
            for(int y = 0; y < input.height(); y++) {
                Position position = new Position(x, y);
                char c = input.getChar(position);
                if(c == '.') continue;
                allAntennas.add(position);
                frequencies.put(position, c);
            }
        }

        for(int i = 1; i < allAntennas.size(); i++) {
            Position firstAntenna = allAntennas.get(i);
            char firstFrequency = frequencies.get(firstAntenna);
            for(int j = 0; j < i; j++) {
                Position secondAntenna = allAntennas.get(j);
                char secondFrequency = frequencies.get(secondAntenna);
                if(firstFrequency != secondFrequency) continue;

                long dx = firstAntenna.getX() - secondAntenna.getX();
                long dy = firstAntenna.getY() - secondAntenna.getY();

                Position firstAntinode = new Position(secondAntenna.getX(), secondAntenna.getY());
                Position secondAntinode = new Position(firstAntenna.getX(), firstAntenna.getY());

                if(!part2) {
                    firstAntinode = new Position(firstAntinode.getX() - dx, firstAntinode.getY() - dy);
                    secondAntinode = new Position(secondAntinode.getX() + dx, secondAntinode.getY() + dy);
                    if(input.inbounds(firstAntinode)) antinodes.add(firstAntinode);
                    if(input.inbounds(secondAntinode)) antinodes.add(secondAntinode);
                    continue;
                }

                while(input.inbounds(firstAntinode)) {
                    antinodes.add(firstAntinode);
                    firstAntinode = new Position(firstAntinode.getX() - dx, firstAntinode.getY() - dy);
                }

                while(input.inbounds(secondAntinode)) {
                    antinodes.add(secondAntinode);
                    secondAntinode = new Position(secondAntinode.getX() + dx, secondAntinode.getY() + dy);
                }}
        }

        return antinodes.size();
    }

}
