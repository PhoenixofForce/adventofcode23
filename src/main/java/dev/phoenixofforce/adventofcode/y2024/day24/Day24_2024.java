package dev.phoenixofforce.adventofcode.y2024.day24;

import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day24_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        Map<String, Boolean> wires = new HashMap<>();
        List<String> gates = new ArrayList<>();
        
        boolean encounteredBlankLine = false;
        for(int i = 0; i < input.getLines().size(); i++) {
            String line = input.getLines().get(i);
            if(line.isEmpty()) {
                encounteredBlankLine = true;
                continue;
            }
            
            if(!encounteredBlankLine) {
                String wireName = line.split(": ")[0];
                boolean state = "1".equals(line.split(": ")[1]);
                wires.put(wireName, state);
                continue;
            }
            gates.add(line);
        }

        long x  = getNumberFromPrefix(wires, "x");
        long y  = getNumberFromPrefix(wires, "y");
        long z  = calculateZ(wires, gates);

        System.out.println("x: " + x);
        System.out.println("y: " + y);
        System.out.println("x+y: " + (x+y));
        System.out.println("==z: " + z);

        return z;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        Map<String, Boolean> wires = new HashMap<>();
        List<String> gates = new ArrayList<>();

        boolean encounteredBlankLine = false;
        for(int i = 0; i < input.getLines().size(); i++) {
            String line = input.getLines().get(i);
            if(line.isEmpty()) {
                encounteredBlankLine = true;
                continue;
            }

            if(!encounteredBlankLine) {
                String wireName = line.split(": ")[0];
                boolean state = "1".equals(line.split(": ")[1]);
                wires.put(wireName, state);
                continue;
            }
            gates.add(line);
        }

        long x  = getNumberFromPrefix(wires, "x");
        long y  = getNumberFromPrefix(wires, "y");

        List<Integer> gatesCalculatingZWithoutXOR = new ArrayList<>();
        List<Integer> gatesXORingWithoutXYZ = new ArrayList<>();
        for (int i = 0; i < gates.size(); i++) {
            String gate = gates.get(i);

            if(gate.matches("[^xyz].. XOR [^xyz].. -> [^xyz]..") ) {
                gatesXORingWithoutXYZ.add(i);
            }

            if(gate.matches("(...|... )[^X].. ... -> z([^4].|4[^5])")) {
                gatesCalculatingZWithoutXOR.add(i);
            }
        }


        int swap0Part1 = gatesCalculatingZWithoutXOR.get(0);
        int swap1Part1 = gatesCalculatingZWithoutXOR.get(1);
        int swap2Part1 = gatesCalculatingZWithoutXOR.get(2);

        Set<String> out = new HashSet<>();
        for(int swap0Part2 : gatesXORingWithoutXYZ) {
            if(Set.of(swap0Part1, swap1Part1, swap2Part1).contains(swap0Part2)) continue;

            String swap0Part1Line = gates.get(swap0Part1);
            String swap0Part1End = swap0Part1Line.split(" -> ")[1];

            String swap0Part2Line = gates.get(swap0Part2);
            String swap0Part2End = swap0Part2Line.split(" -> ")[1];

            String swappedLine01 = swap0Part1Line.split(" -> ")[0] + " -> " + swap0Part2End;
            String swappedLine02 = swap0Part2Line.split(" -> ")[0] + " -> " + swap0Part1End;


            for(int swap1Part2 : gatesXORingWithoutXYZ) {
                if(Set.of(swap0Part1, swap1Part1, swap2Part1, swap0Part2).contains(swap1Part2)) continue;

                String swap1Part1Line = gates.get(swap1Part1);
                String swap1Part1End = swap1Part1Line.split(" -> ")[1];

                String swap1Part2Line = gates.get(swap1Part2);
                String swap1Part2End = swap1Part2Line.split(" -> ")[1];

                String swappedLine11 = swap1Part1Line.split(" -> ")[0] + " -> " + swap1Part2End;
                String swappedLine12 = swap1Part2Line.split(" -> ")[0] + " -> " + swap1Part1End;

                for(int swap2Part2 : gatesXORingWithoutXYZ) {
                    if(Set.of(swap0Part1, swap1Part1, swap2Part1, swap0Part2, swap1Part2).contains(swap2Part2)) continue;

                    String swap2Part1Line = gates.get(swap2Part1);
                    String swap2Part1End = swap2Part1Line.split(" -> ")[1];

                    String swap2Part2Line = gates.get(swap2Part2);
                    String swap2Part2End = swap2Part2Line.split(" -> ")[1];

                    String swappedLine21 = swap2Part1Line.split(" -> ")[0] + " -> " + swap2Part2End;
                    String swappedLine22 = swap2Part2Line.split(" -> ")[0] + " -> " + swap2Part1End;

                    for(int swap3Part1 = 1; swap3Part1 < gates.size(); swap3Part1++) {
                        if(Set.of(swap0Part1, swap0Part2, swap1Part1, swap1Part2, swap2Part1, swap2Part2).contains(swap3Part1)) continue;

                        for(int swap3Part2 = 0; swap3Part2 < swap3Part1; swap3Part2++) {
                            if(Set.of(swap0Part1, swap0Part2, swap1Part1, swap1Part2, swap2Part1, swap2Part2, swap3Part1).contains(swap3Part2)) continue;

                            String swap3Part1Line = gates.get(swap3Part1);
                            String swap3Part1End = swap3Part1Line.split(" -> ")[1];

                            String swap3Part2Line = gates.get(swap3Part2);
                            String swap3Part2End = swap3Part2Line.split(" -> ")[1];

                            if(swap3Part1End.contains("z") || swap3Part2End.contains("z")) continue;

                            String swappedLine31 = swap3Part1Line.split(" -> ")[0] + " -> " + swap3Part2End;
                            String swappedLine32 = swap3Part2Line.split(" -> ")[0] + " -> " + swap3Part1End;

                            List<String> newGates = new ArrayList<>(gates);
                            newGates.set(swap0Part1, swappedLine01);
                            newGates.set(swap0Part2, swappedLine02);

                            newGates.set(swap1Part1, swappedLine11);
                            newGates.set(swap1Part2, swappedLine12);

                            newGates.set(swap2Part1, swappedLine21);
                            newGates.set(swap2Part2, swappedLine22);

                            newGates.set(swap3Part1, swappedLine31);
                            newGates.set(swap3Part2, swappedLine32);

                            long z = calculateZ(wires, newGates);

                            //TODO: better detection
                            if(x + y != z) continue;
                            System.out.println(swap3Part1 + " <> " + swap3Part2);
                            System.out.println("x: " + x);
                            System.out.println("y: " + y);
                            System.out.println("x+y: " + (x+y));
                            System.out.println("==z: " + z);

                            List<String> swappingWires = List.of(
                                swap0Part1End, swap0Part2End,
                                swap1Part1End, swap1Part2End,
                                swap2Part1End, swap2Part2End,
                                swap3Part1End, swap3Part2End
                            );

                            String possibleOut = swappingWires.stream()
                                .sorted()
                                .toList()
                                .toString()
                                .replaceAll("[\\[\\] ]", "");

                            if(out.contains(possibleOut)) continue;
                            out.add(possibleOut);
                            System.out.println("--------------------");
                            System.out.println(possibleOut);
                            System.out.println("--------------------");
                        }
                    }
                }
            }
        }

        return out.stream().findFirst().orElse("");
    }

    private long calculateZ(Map<String, Boolean> wiresIn, List<String> gateIn) {
        Map<String, Boolean> wires = new HashMap<>(wiresIn);
        List<String> gates = new ArrayList<>(gateIn);

        while(!gates.isEmpty()) {
            List<String> toRemove = new ArrayList<>();

            for(String gate: gates) {
                String[] parts = gate.split(" ");
                String firstArg = parts[0];
                String operand = parts[1];
                String secondArg = parts[2];
                String result = parts[4];

                if(!wires.containsKey(firstArg) || !wires.containsKey(secondArg)) {
                    continue;
                }

                boolean firstWire = wires.get(firstArg);
                boolean secondWire = wires.get(secondArg);
                boolean resultingWire = firstWire && secondWire;

                if("OR".equals(operand)) resultingWire = firstWire || secondWire;
                else if("XOR".equals(operand)) resultingWire = firstWire != secondWire;

                wires.put(result, resultingWire);
                toRemove.add(gate);
            }
            gates.removeAll(toRemove);
            if(toRemove.isEmpty() && !gates.isEmpty()) return -1;
        }

        return getNumberFromPrefix(wires, "z");
    }

    private long getNumberFromPrefix(Map<String, Boolean> wires, String prefix) {
        String out = "";
        int index = 0;

        String indexString = "0" + index;
        while(wires.containsKey(prefix + indexString)) {
            boolean state = wires.get(prefix + indexString);
            out = (state ? "1" : "0") + out;

            index++;
            indexString = index + "";
            if(indexString.length() == 1) indexString = "0" + indexString;
        }

        return Long.parseLong(out, 2);
    }

}
