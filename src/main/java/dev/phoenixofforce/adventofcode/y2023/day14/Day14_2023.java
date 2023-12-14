package dev.phoenixofforce.adventofcode.y2023.day14;

import dev.phoenixofforce.adventofcode.common.ArrayUtils;
import dev.phoenixofforce.adventofcode.common.Puzzle;
import dev.phoenixofforce.adventofcode.common.PuzzleInput;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Day14_2023 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        char[][] map = ArrayUtils.strings2CharArray(input.getLines());
        tiltNorth(map);
        System.out.println(ArrayUtils.toString(map));
        return calculateLoad(map);
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        char[][] map = ArrayUtils.strings2CharArray(input.getLines());
        int cycles = 1000000000;

        Map<Long, Integer> cycleHashes = new HashMap<>();
        for(int i = 0; i < cycles; i++) {
            long hash = hash(map);
            if(cycleHashes.containsKey(hash)) {
                System.out.println("cycle from " + cycleHashes.get(hash));
                int cycleLength = i - cycleHashes.get(hash);
                while(i + cycleLength < cycles) i += cycleLength;
            }
            else cycleHashes.put(hash, i);

            if(i % 1000 == 0) System.out.println(i + "/" + cycles + "(" + (100.0 * i/cycles) + ")");
            tiltNorth(map);
            tiltWest(map);
            tiltSouth(map);
            tiltEast(map);
        }
        System.out.println(ArrayUtils.toString(map));


        return calculateLoad(map);
    }

    private void tiltNorth(char[][] map) {
        for(int x = 0; x < map[0].length; x++) {
            for(int y = 1; y < map.length; y++) {
                if(map[y][x] == 'O') {
                    int newY = y;
                    while(newY > 0 && map[newY - 1][x] == '.') {
                        newY--;
                    }

                    if(newY != y) {
                        map[newY][x] = 'O';
                        map[y][x] = '.';
                    }
                }
            }
        }
    }

    private void tiltSouth(char[][] map) {
        for(int x = 0; x < map[0].length; x++) {
            for(int y = map.length - 2; y >= 0; y--) {
                if(map[y][x] == 'O') {
                    int newY = y;
                    while(newY < map.length - 1 && map[newY + 1][x] == '.') {
                        newY++;
                    }

                    if(newY != y) {
                        map[newY][x] = 'O';
                        map[y][x] = '.';
                    }
                }
            }
        }
    }

    private void tiltWest(char[][] map) {
        for(int y = 0; y < map.length; y++) {
            for(int x = 1; x < map[0].length; x++) {
                if(map[y][x] == 'O') {
                    int newX = x;
                    while(newX > 0 && map[y][newX - 1] == '.') {
                        newX--;
                    }

                    if(newX != x) {
                        map[y][newX] = 'O';
                        map[y][x] = '.';
                    }
                }
            }
        }
    }

    private void tiltEast(char[][] map) {
        for(int y = 0; y < map.length; y++) {
            for(int x = map[0].length - 2; x >= 0; x--) {
                if(map[y][x] == 'O') {
                    int newX = x;
                    while(newX < map[0].length - 1 && map[y][newX + 1] == '.') {
                        newX++;
                    }

                    if(newX != x) {
                        map[y][newX] = 'O';
                        map[y][x] = '.';
                    }
                }
            }
        }
    }

    private long hash(char[][] map) {
        long out = 17;
        for(int x = 0; x < map[0].length; x++) {
            for (int y = 0; y < map.length; y++) {
                if (map[y][x] == 'O') {
                    out = out * 31 + x;
                    out = out * 31 + y;
                }
            }
        }
        return out * 31L;
    }

    private long calculateLoad(char[][] map) {
        long out = 0;
        for(int x = 0; x < map[0].length; x++) {
            for (int y = 0; y < map.length; y++) {
                if (map[y][x] == 'O') {
                    out += map.length - y;
                }
            }
        }
        return out;
    }

}
