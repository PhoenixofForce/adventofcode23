package dev.phoenixofforce.adventofcode.y2023.day02;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Game {

    private String line;

    private int id;
    private final List<int[]> ballsDrawn = new ArrayList<>();

    public Game(String line) {
        this.line = line;
        this.id = Integer.parseInt(line.split(" ")[1].split(":")[0]);

        String[] randomPulls = line.split(":")[1].trim().split(";");
        for (String thisGamesPulls : randomPulls) {
            String[] thisPull = thisGamesPulls.trim().split(",");
            int[] thisPullAmount = new int[3];

            for (String singlePull : thisPull) {
                int amount = Integer.parseInt(singlePull.trim().split(" ")[0]);
                String color = singlePull.trim().split(" ")[1];
                int index = switch (color) {
                    case "red" -> 0;
                    case "green" -> 1;
                    case "blue" -> 2;
                    default -> -1;
                };

                if (index >= 0) thisPullAmount[index] = amount;
            }

            ballsDrawn.add(thisPullAmount);
        }
    }

    public boolean isPossibleWithGivenAmount(int targetRed, int targetGreen, int targetBlue) {
        return ballsDrawn.stream().allMatch(
            balls1 -> balls1[0] <= targetRed && balls1[1] <= targetGreen && balls1[2] <= targetBlue
        );
    }

    public long getPower() {
        return getMaximumRevealedCubesForIndex(0) * getMaximumRevealedCubesForIndex(1) * getMaximumRevealedCubesForIndex(2);
    }

    private long getMaximumRevealedCubesForIndex(int i) {
        return ballsDrawn.stream()
            .mapToInt(b -> b[i])
            .max().orElse(0);
    }
}
