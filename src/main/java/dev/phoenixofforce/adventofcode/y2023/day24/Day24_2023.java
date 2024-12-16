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

        Equation[] stones = new Equation[]{
            equations.get(0),
            equations.get(1),
            equations.get(2)
        };

        // Berechnung der Differenzen und Kreuzprodukte
        BigDecimal V0x = stones[0].getVelX(), V0y = stones[0].getVelY(), V0z = stones[0].getVelZ();
        BigDecimal V1x = stones[1].getVelX(), V1y = stones[1].getVelY(), V1z = stones[1].getVelZ();
        BigDecimal V2x = stones[2].getVelX(), V2y = stones[2].getVelY(), V2z = stones[2].getVelZ();

        BigDecimal P0x = stones[0].getPosX(), P0y = stones[0].getPosY(), P0z = stones[0].getPosZ();
        BigDecimal P1x = stones[1].getPosX(), P1y = stones[1].getPosY(), P1z = stones[1].getPosZ();
        BigDecimal P2x = stones[2].getPosX(), P2y = stones[2].getPosY(), P2z = stones[2].getPosZ();

        // Kreuzprodukt (V0 - V1) × (P0 - P1)
        BigDecimal cross1x = (V0y.subtract(V1y)).multiply(P0z.subtract(P1z)).subtract((V0z.subtract(V1z)).multiply(P0y.subtract(P1y)));
        BigDecimal cross1y = (V0z.subtract(V1z)).multiply(P0x.subtract(P1x)).subtract((V0x.subtract(V1x)).multiply(P0z.subtract(P1z)));
        BigDecimal cross1z = (V0x.subtract(V1x)).multiply(P0y.subtract(P1y)).subtract((V0y.subtract(V1y)).multiply(P0x.subtract(P1x)));

        // Kreuzprodukt (V0 - V2) × (P0 - P2)
        BigDecimal cross2x = (V0y.subtract(V2y)).multiply(P0z.subtract(P2z)).subtract((V0z.subtract(V2z)).multiply(P0y.subtract(P2y)));
        BigDecimal cross2y = (V0z.subtract(V2z)).multiply(P0x.subtract(P2x)).subtract((V0x.subtract(V2x)).multiply(P0z.subtract(P2z)));
        BigDecimal cross2z = (V0x.subtract(V2x)).multiply(P0y.subtract(P2y)).subtract((V0y.subtract(V2y)).multiply(P0x.subtract(P2x)));

        // Kreuzprodukt (V1 - V2) × (P1 - P2)
        BigDecimal cross3x = (V1y.subtract(V2y)).multiply(P1z.subtract(P2z)).subtract((V1z.subtract(V2z)).multiply(P1y.subtract(P2y)));
        BigDecimal cross3y = (V1z.subtract(V2z)).multiply(P1x.subtract(P2x)).subtract((V1x.subtract(V2x)).multiply(P1z.subtract(P2z)));
        BigDecimal cross3z = (V1x.subtract(V2x)).multiply(P1y.subtract(P2y)).subtract((V1y.subtract(V2y)).multiply(P1x.subtract(P2x)));

        // Matrix C, deren Zeilen die Kreuzprodukte der Geschwindigkeitsdifferenzen sind
        BigDecimal[][] C = {
            {cross1x, cross1y, cross1z},
            {cross2x, cross2y, cross2z},
            {cross3x, cross3y, cross3z}
        };

        // Vektor D mit den Skalarprodukten (V0 - V1) ⋅ P0 × P1, (V0 - V2) ⋅ P0 × P2, (V1 - V2) ⋅ P1 × P2
        BigDecimal D0 = (V0x.subtract(V1x)).multiply(P0x.subtract(P1x))
            .add((V0y.subtract(V1y)).multiply(P0y.subtract(P1y)))
            .add((V0z.subtract(V1z)).multiply(P0z.subtract(P1z)));
        BigDecimal D1 = (V0x.subtract(V2x)).multiply(P0x.subtract(P2x))
            .add((V0y.subtract(V2y)).multiply(P0y.subtract(P2y)))
            .add((V0z.subtract(V2z)).multiply(P0z.subtract(P2z)));
        BigDecimal D2 = (V1x.subtract(V2x)).multiply(P1x.subtract(P2x))
            .add((V1y.subtract(V2y)).multiply(P1y.subtract(P2y)))
            .add((V1z.subtract(V2z)).multiply(P1z.subtract(P2z)));

        BigDecimal[] D = {D0, D1, D2};

        // Berechnung der Determinante (C^-1 D)
        BigDecimal det = cross1x.multiply(cross2y).multiply(cross3z)
            .add(cross1y.multiply(cross2z).multiply(cross3x))
            .add(cross1z.multiply(cross2x).multiply(cross3y))
            .subtract(cross1z.multiply(cross2y).multiply(cross3x))
            .subtract(cross1y.multiply(cross2x).multiply(cross3z))
            .subtract(cross1x.multiply(cross2z).multiply(cross3y));

        // Inverse der Determinante
        BigDecimal det_inv = BigDecimal.ONE.divide(det, MathContext.DECIMAL128);

        // Berechnung von x, y, z (P = C^-1 D)
        BigDecimal Px = det_inv.multiply(cross1x.multiply(cross2y).multiply(cross3z)
            .subtract(cross1y.multiply(cross2z).multiply(cross3x))
            .subtract(cross1z.multiply(cross2x).multiply(cross3y)));
        BigDecimal Py = det_inv.multiply(cross1x.multiply(cross2z).multiply(cross3y)
            .subtract(cross1y.multiply(cross2x).multiply(cross3z))
            .subtract(cross1z.multiply(cross2y).multiply(cross3x)));
        BigDecimal Pz = det_inv.multiply(cross1x.multiply(cross2y).multiply(cross3z)
            .subtract(cross1y.multiply(cross2z).multiply(cross3x))
            .subtract(cross1z.multiply(cross2x).multiply(cross3y)));

        System.out.println(Px);
        System.out.println(Py);
        System.out.println(Pz);

        return Px.add(Py).add(Pz);
    }



}
