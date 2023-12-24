package dev.phoenixofforce.adventofcode.y2023.day24;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

@Component
public class Day24_2023 implements Puzzle {

    private static MathContext context = MathContext.DECIMAL32;

    @Data @EqualsAndHashCode @AllArgsConstructor
    private static class Equation {
        BigDecimal posX, posY, posZ;
        BigDecimal velX, velY, velZ;

        public Equation(String line) {
            String[] positionParts = line.split(" @ ")[0].split(",");
            String[] velocityParts = line.split(" @ ")[1].split(",");

             posX = new BigDecimal(positionParts[0].trim(), context);
             posY = new BigDecimal(positionParts[1].trim(), context);
             posZ = new BigDecimal(positionParts[2].trim(), context);

            velX = new BigDecimal(velocityParts[0].trim(), context);
            velY = new BigDecimal(velocityParts[1].trim(), context);
            velZ = new BigDecimal(velocityParts[2].trim(), context);
        }
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        List<Equation> equations = input.getLines().stream()
            .map(Equation::new)
            .toList();

        BigDecimal borderMin = new BigDecimal("200000000000000");
        BigDecimal borderMax = new BigDecimal("400000000000000");

        long out = 0;
        for(int i = 1; i < equations.size(); i++) {
            Equation eq1 = equations.get(i);
            for(int j = 0; j < i; j++) {
                Equation eq2 = equations.get(j);
                BigDecimal[] intersection = intersectsXY(eq1, eq2);

                System.out.println(eq1 + " " + eq2 + " cross at ");
                if(intersection.length != 0) {

                    BigDecimal x = intersection[0];
                    BigDecimal y = intersection[1];

                    System.out.println("\t(" + x + " | " + y + ")");

                    if(x.compareTo(borderMin) >= 0 && x.compareTo(borderMax) < 0 &&
                        y.compareTo(borderMin) >= 0 && y.compareTo(borderMax) < 0) {
                        System.out.println("\tthat is inside");
                        out++;
                    }
                } else {
                    System.out.println("\twont cross or in the past");
                }
            }
        }

        return out;
    }

    private BigDecimal[] intersectsXY(Equation eq, Equation border) {
        BigDecimal below = border.velY.multiply(eq.velX).subtract(border.velX.multiply(eq.velY));
        if(below.equals(BigDecimal.ZERO)) return new BigDecimal[]{};

        BigDecimal i = eq.posX.multiply(BigDecimal.valueOf(-1)).multiply(border.velY).add(
            eq.posY.multiply(border.velX)
        ).add(
            border.posX.multiply(border.velY)
        ).subtract(
            border.posY.multiply(border.velX)
        );
        i = i.divide(below, context);

        BigDecimal j = eq.posX.multiply(BigDecimal.valueOf(-1)).multiply(eq.velY).add(
            eq.posY.multiply(eq.velX)
        ).add(
            border.posX.multiply(eq.velY)
        ).subtract(
            border.posY.multiply(eq.velX)
        );
        j = j.divide(below, context);

        if(i.compareTo(BigDecimal.ZERO) < 0 || j.compareTo(BigDecimal.ZERO) < 0) return new BigDecimal[]{};

        return new BigDecimal[]{
            eq.posX.add(eq.velX.multiply(i)),
            eq.posY.add(eq.velY.multiply(i))
        };
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        List<Equation> equations = input.getLines().stream()
            .map(Equation::new)
            .toList();

        Equation[] pair1 = new Equation[]{
            equations.get(0),
            equations.get(1)
        };

        Equation[] pair2 = new Equation[]{
            equations.get(0),
            equations.get(2)
        };

        return "";
    }



}
