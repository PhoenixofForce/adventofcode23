package dev.phoenixofforce.adventofcode.y2025.day08;

import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Slf4j
@Component
public class Day08_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Position> junctionBoxes = input.getLines()
                .stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Position(Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2]));
                })
                .toList();

        List<Set<Position>> circuits = new ArrayList<>(
                junctionBoxes.stream()
                .map(e -> {
                    Set<Position> s = new HashSet<>();
                    s.add(e);
                    return s;
                }).toList()
        );

        int pairsToConnect = 1000 ;
        double maxDistanceFound = 0; // todo: maybe use different mechanism to avoid dupes
        for(int c = 0; c < pairsToConnect; c++) {
            double currentShortestConnection = Long.MAX_VALUE;

            Position p1 = null, p2 = null;
            for(int i = 0; i < junctionBoxes.size() - 1; i++) {
                for(int j = i + 1; j < junctionBoxes.size(); j++) {
                    Position first = junctionBoxes.get(i);
                    Position second = junctionBoxes.get(j);
                    double distance = first.distance(second);

                    if(distance > maxDistanceFound && distance < currentShortestConnection) {
                        currentShortestConnection = distance;
                        p1 = first;
                        p2 = second;
                    }
                }
            }
            if(p1 == null || p2 == null) break;

            maxDistanceFound = currentShortestConnection;
            Position finalP1 = p1;
            Position finalP2 = p2;
            List<Set<Position>> existingCircuits = circuits.stream()
                    .filter(e -> e.contains(finalP1) || e.contains(finalP2))
                    .toList();

            if(existingCircuits.size() == 1) continue;
            Set<Position> combinedCircuit = new HashSet<>();
            for(Set<Position> circuit : existingCircuits) {
                combinedCircuit.addAll(circuit);
                circuits.remove(circuit);
            }
            circuits.add(combinedCircuit);
        }

        log.info("Found {} circuits in {} steps with sizes {}", circuits.size(), pairsToConnect, circuits.stream().map(Set::size).sorted(Comparator.comparingInt(e -> (int) e).reversed()).toList());

        return circuits.stream()
                .map(Set::size)
                .sorted(Comparator.comparingInt(e -> (int) e).reversed())
                .limit(3)
                .reduce(1, (a, b) -> a * b);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<Position> junctionBoxes = input.getLines()
                .stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Position(Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2]));
                })
                .toList();

        List<Set<Position>> circuits = new ArrayList<>(
                junctionBoxes.stream()
                        .map(e -> {
                            Set<Position> s = new HashSet<>();
                            s.add(e);
                            return s;
                        }).toList()
        );

        double maxDistanceFound = 0;
        long lastX = 0;
        while (circuits.size() > 1){
            double currentShortestConnection = Long.MAX_VALUE;

            Position p1 = null, p2 = null;
            for(int i = 0; i < junctionBoxes.size() - 1; i++) {
                for(int j = i + 1; j < junctionBoxes.size(); j++) {
                    Position first = junctionBoxes.get(i);
                    Position second = junctionBoxes.get(j);
                    double distance = first.distance(second);

                    if(distance > maxDistanceFound && distance < currentShortestConnection) {
                        currentShortestConnection = distance;
                        p1 = first;
                        p2 = second;
                    }
                }
            }
            if(p1 == null || p2 == null) break;

            lastX = p1.getX() * p2.getX();
            maxDistanceFound = currentShortestConnection;
            Position finalP1 = p1;
            Position finalP2 = p2;
            List<Set<Position>> existingCircuits = circuits.stream()
                    .filter(e -> e.contains(finalP1) || e.contains(finalP2))
                    .toList();

            if(existingCircuits.size() == 1) continue;
            Set<Position> combinedCircuit = new HashSet<>();
            for(Set<Position> circuit : existingCircuits) {
                combinedCircuit.addAll(circuit);
                circuits.remove(circuit);
            }
            circuits.add(combinedCircuit);
        }

        return lastX;
    }

}
