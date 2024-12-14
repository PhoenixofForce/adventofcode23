package dev.phoenixofforce.adventofcode.y2024.day14;

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
public class Day14_2024 implements Puzzle {

    @Data
    @AllArgsConstructor
    private static class Robot {
        private Position position;
        private Position velocity;
    }

    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;
    private static final int SIMULATIONS = 100;

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Robot> robots = input.getLines()
            .stream()
            .map(e -> e.replaceAll("[pv=]", ""))
            .map(e -> {
                String[] positionVelocity = e.split(" ");
                String[] positionValues = positionVelocity[0].split(",");
                String[] velocityValues = positionVelocity[1].split(",");
                return new Robot(
                    new Position(Long.parseLong(positionValues[0]), Long.parseLong(positionValues[1])),
                    new Position(Long.parseLong(velocityValues[0]), Long.parseLong(velocityValues[1]))
                );
            }).toList();

        for(int i = 0; i < SIMULATIONS; i++) {
            for(Robot r: robots) {
                long newX = r.getPosition().getX() + r.getVelocity().getX();
                long newY = r.getPosition().getY() + r.getVelocity().getY();

                r.setPosition(wrapPosition(newX, newY));
            }
        }

        int[] robotsInQuadrant = {0, 0, 0, 0};
        for(Robot r: robots) {
            int index = -1;
            if(r.getPosition().getX() < (WIDTH - 1) / 2 && r.getPosition().getY() < (HEIGHT - 1) / 2) {
                index = 0;
            } else if(r.getPosition().getX() > (WIDTH - 1) / 2 && r.getPosition().getY() < (HEIGHT - 1) / 2) {
                index = 1;
            } else if(r.getPosition().getX() < (WIDTH - 1) / 2 && r.getPosition().getY() > (HEIGHT - 1) / 2) {
                index = 2;
            } if(r.getPosition().getX() > (WIDTH - 1) / 2 && r.getPosition().getY() > (HEIGHT - 1) / 2) {
                index = 3;
            }

            if(index != -1) robotsInQuadrant[index]++;
        }

        return robotsInQuadrant[0] * robotsInQuadrant[1] * robotsInQuadrant[2] * robotsInQuadrant[3];
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<Robot> robots = input.getLines()
            .stream()
            .map(e -> e.replaceAll("[pv=]", ""))
            .map(e -> {
                String[] positionVelocity = e.split(" ");
                String[] positionValues = positionVelocity[0].split(",");
                String[] velocityValues = positionVelocity[1].split(",");
                return new Robot(
                    new Position(Long.parseLong(positionValues[0]), Long.parseLong(positionValues[1])),
                    new Position(Long.parseLong(velocityValues[0]), Long.parseLong(velocityValues[1]))
                );
            }).toList();

        long i = 0;
        while(true) {
            if(i % 100 == 0) System.out.println(i);
            i++;

            for(Robot r: robots) {
                long newX = r.getPosition().getX() + r.getVelocity().getX();
                long newY = r.getPosition().getY() + r.getVelocity().getY();

                r.setPosition(wrapPosition(newX, newY));
            }

            Set<Position> positionToRobot = robots.stream().map(Robot::getPosition).collect(Collectors.toSet());
            for(Robot r: robots) {
                if(createBucket(positionToRobot, r).size() < 50) {
                    continue;
                }

                for(int y = 0;  y < HEIGHT; y++) {
                    for(int x = 0; x < WIDTH; x++) {
                        char c = ' ';
                        if(positionToRobot.contains(new Position(x, y))) c =  '#';
                        System.out.print(c);
                    }
                    System.out.println();
                }

                return i;
            }
        }
    }

    private Set<Position> createBucket(Set<Position> positionToRobot, Robot start) {
        Set<Position> bucket = new HashSet<>();
        List<Position> open = new ArrayList<>();
        open.add(start.getPosition());

        while(!open.isEmpty()) {
            Position current = open.removeFirst();
            if(bucket.contains(current)) continue;
            bucket.add(current);

            for(Position next: Direction.getNeighbors4(Position::new, (int) current.getX(), (int) current.getY())) {
                Position pos = wrapPosition(next.getX(), next.getY());

                if(!positionToRobot.contains(pos)) continue;
                if(open.contains(pos) || bucket.contains(pos)) continue;

                open.add(pos);
            }
        }
        return bucket;
    }

    private Position wrapPosition(long newX, long newY) {
        newX %= WIDTH;
        while(newX < 0) newX += WIDTH;

        newY %= HEIGHT;
        while(newY < 0) newY += HEIGHT;
        return new Position(newX, newY);
    }
}
